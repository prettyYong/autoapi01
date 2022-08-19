/*
package com.tester.config;


import com.aventstack.extentreports.extentreports;
import com.aventstack.extentreports.extenttest;
import com.aventstack.extentreports.resourcecdn;
import com.aventstack.extentreports.status;
import com.aventstack.extentreports.model.testattribute;
import com.aventstack.extentreports.reporter.extenthtmlreporter;
import com.aventstack.extentreports.reporter.configuration.chartlocation;
import com.aventstack.extentreports.reporter.configuration.theme;
import org.testng.*;
import org.testng.xml.xmlsuite;

import java.io.file;
import java.util.*;

public class extenttestngreportlistener implements ireporter {
    //生成的路径以及文件名
    private static final string output_folder = "test-output/";
    private static final string file_name = "index.html";

    private extentreports extent;

    @override
    public void generatereport(list<xmlsuite> xmlsuites, list<isuite> suites, string outputdirectory) {
        init();
        boolean createsuitenode = false;
        if (suites.size() > 1) {
            createsuitenode = true;
        }
        for (isuite suite : suites) {
            map<string, isuiteresult> result = suite.getresults();
            //如果suite里面没有任何用例，直接跳过，不在报告里生成
            if (result.size() == 0) {
                continue;
            }
            //统计suite下的成功、失败、跳过的总用例数
            int suitefailsize = 0;
            int suitepasssize = 0;
            int suiteskipsize = 0;
            extenttest suitetest = null;
            //存在多个suite的情况下，在报告中将同一个一个suite的测试结果归为一类，创建一级节点。
            if (createsuitenode) {
                suitetest = extent.createtest(suite.getname()).assigncategory(suite.getname());
            }
            boolean createsuiteresultnode = false;
            if (result.size() > 1) {
                createsuiteresultnode = true;
            }
            for (isuiteresult r : result.values()) {
                extenttest resultnode;
                itestcontext context = r.gettestcontext();
                if (createsuiteresultnode) {
                    //没有创建suite的情况下，将在suiteresult的创建为一级节点，否则创建为suite的一个子节点。
                    if (null == suitetest) {
                        resultnode = extent.createtest(r.gettestcontext().getname());
                    } else {
                        resultnode = suitetest.createnode(r.gettestcontext().getname());
                    }
                } else {
                    resultnode = suitetest;
                }
                if (resultnode != null) {
                    resultnode.getmodel().setname(suite.getname() + " : " + r.gettestcontext().getname());
                    if (resultnode.getmodel().hascategory()) {
                        resultnode.assigncategory(r.gettestcontext().getname());
                    } else {
                        resultnode.assigncategory(suite.getname(), r.gettestcontext().getname());
                    }
                    resultnode.getmodel().setstarttime(r.gettestcontext().getstartdate());
                    resultnode.getmodel().setendtime(r.gettestcontext().getenddate());
                    //统计suiteresult下的数据
                    int passsize = r.gettestcontext().getpassedtests().size();
                    int failsize = r.gettestcontext().getfailedtests().size();
                    int skipsize = r.gettestcontext().getskippedtests().size();
                    suitepasssize += passsize;
                    suitefailsize += failsize;
                    suiteskipsize += skipsize;
                    if (failsize > 0) {
                        resultnode.getmodel().setstatus(status.fail);
                    }
                    resultnode.getmodel().setdescription(string.format("pass: %s ; fail: %s ; skip: %s ;", passsize, failsize, skipsize));
                }
                buildtestnodes(resultnode, context.getfailedtests(), status.fail);
                buildtestnodes(resultnode, context.getskippedtests(), status.skip);
                buildtestnodes(resultnode, context.getpassedtests(), status.pass);
            }
            if (suitetest != null) {
                suitetest.getmodel().setdescription(string.format("pass: %s ; fail: %s ; skip: %s ;", suitepasssize, suitefailsize, suiteskipsize));
                if (suitefailsize > 0) {
                    suitetest.getmodel().setstatus(status.fail);
                }
            }

        }
//        for (string s : reporter.getoutput()) {
//            extent.settestrunneroutput(s);
//        }

        extent.flush();
    }

    private void init() {
        //文件夹不存在的话进行创建
        file reportdir = new file(output_folder);
        if (!reportdir.exists() && !reportdir.isdirectory()) {
            reportdir.mkdir();
        }
        extenthtmlreporter htmlreporter = new extenthtmlreporter(output_folder + file_name);
        // 设置静态文件的dns
        //怎么样解决cdn.rawgit.com访问不了的情况
        htmlreporter.config().setresourcecdn(resourcecdn.extentreports);

        htmlreporter.config().setdocumenttitle("api自动化测试报告");
        htmlreporter.config().setreportname("api自动化测试报告");
        htmlreporter.config().setchartvisibilityonopen(true);
        htmlreporter.config().settestviewchartlocation(chartlocation.top);
        htmlreporter.config().settheme(theme.standard);
        htmlreporter.config().setcss(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
        extent = new extentreports();
        extent.attachreporter(htmlreporter);
        extent.setreportusesmanualconfiguration(true);
    }

    private void buildtestnodes(extenttest extenttest, iresultmap tests, status status) {
        //存在父节点时，获取父节点的标签
        string[] categories = new string[0];
        if (extenttest != null) {
            list<testattribute> categorylist = extenttest.getmodel().getcategorycontext().getall();
            categories = new string[categorylist.size()];
            for (int index = 0; index < categorylist.size(); index++) {
                categories[index] = categorylist.get(index).getname();
            }
        }

        extenttest test;

        if (tests.size() > 0) {
            //调整用例排序，按时间排序
            set<itestresult> treeset = new treeset<itestresult>(new comparator<itestresult>() {
                @override
                public int compare(itestresult o1, itestresult o2) {
                    return o1.getstartmillis() < o2.getstartmillis() ? -1 : 1;
                }
            });
            treeset.addall(tests.getallresults());
            for (itestresult result : treeset) {
                object[] parameters = result.getparameters();
                string name = "";
                //如果有参数，则使用参数的tostring组合代替报告中的name
                for (object param : parameters) {
                    name += param.tostring();
                }
                if (name.length() > 0) {
                    if (name.length() > 50) {
                        name = name.substring(0, 49) + "...";
                    }
                } else {
                    name = result.getmethod().getmethodname();
                }
                if (extenttest == null) {
                    test = extent.createtest(name);
                } else {
                    //作为子节点进行创建时，设置同父节点的标签一致，便于报告检索。
                    test = extenttest.createnode(name).assigncategory(categories);
                }
                //test.getmodel().setdescription(description.tostring());
                //test = extent.createtest(result.getmethod().getmethodname());
                for (string group : result.getmethod().getgroups())
                    test.assigncategory(group);

                list<string> outputlist = reporter.getoutput(result);
                for (string output : outputlist) {
                    //将用例的log输出报告中
                    test.debug(output);
                }
                if (result.getthrowable() != null) {
                    test.log(status, result.getthrowable());
                } else {
                    test.log(status, "test " + status.tostring().tolowercase() + "ed");
                }

                test.getmodel().setstarttime(gettime(result.getstartmillis()));
                test.getmodel().setendtime(gettime(result.getendmillis()));
            }
        }
    }

    private date gettime(long millis) {
        calendar calendar = calendar.getinstance();
        calendar.settimeinmillis(millis);
        return calendar.gettime();
    }
}*/
