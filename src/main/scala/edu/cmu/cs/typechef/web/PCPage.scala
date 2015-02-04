package edu.cmu.cs.typechef.web

import java.io.FileWriter

import de.fosd.typechef.featureexpr._
import de.fosd.typechef.featureexpr.bdd.BDDFeatureExpr

import scala.xml.NodeSeq

trait PCPage extends Utils {


    def renderPC(pcstr: String) = {
        val fexpr = new FeatureExprParser(FeatureExprFactory.bdd).parse(pcstr)
        val fms = ("Plain", FeatureExprFactory.bdd.featureModelFactory.empty) :: FileManager.currentProject.featureModels(FeatureExprFactory.bdd)
        val solutions = fexpr.asInstanceOf[BDDFeatureExpr].getBddAllSat.toList.sortBy(_.length)
        val (sel, desel) = computeBackbone(fexpr)

            mkPage("PC: "+pcstr, <div>

                <h1>Presence Condition</h1>

                <pre>{fexpr.toString}</pre>

                <h2>Analysis</h2>

                <div>
                    {for ((n, fm) <- fms) yield
                        <div>{n}: {status(fexpr, fm)}</div>}
                </div>

                <h2>Solutions</h2>

                <div><ul>{
                      for (cnf <- solutions.take(10)) yield
                          <li>{cnf.map(l => if (l._1 == 0) "!" + l._2 else l._2).mkString(" && ")}</li>
                    }

                </ul></div>

                <h2>Backbone</h2>

                <div><ul>{
                      for (s <- sel) yield
                          <li>{s}</li>
                      for (s <- desel) yield
                          <li>! {s}</li>
                }
                </ul></div>

                <h2>Config</h2>
                 <a href={"/genConfig/"+urlEncode(fexpr.toString())}>generate configuration</a>

             </div>)
         }


    private def status(fexpr: FeatureExpr, fm: FeatureModel): NodeSeq =
        if (!fexpr.isSatisfiable(fm)) <span style="color:red">contradiction</span>
        else if (fexpr.isTautology(fm)) <span style="color:red">tautology</span>
        else <span>satisfiable</span>


    private def computeBackbone(expr: FeatureExpr) = {
        var sel = Set[String]()
        var desel = Set[String]()
        for (v<-expr.collectDistinctFeatureObjects) {
            if ((expr and v).isContradiction())
                desel += v.toString()
            if ((expr andNot v).isContradiction())
                sel += v.toString()
        }
        (sel, desel)
    }


    def generateConfig(pcstr: String) = {
        val fexpr = new FeatureExprParser(FeatureExprFactory.bdd).parse(pcstr)

        val formulaFile = new java.io.File(FileManager.currentProject.rootDir, "formula")
        println("writing " + pcstr+ "  in "+formulaFile)
        val writer = new FileWriter(formulaFile)
        fexpr.print(writer)
        writer.close()

        println("running sh mkconfig.sh in "+ FileManager.currentProject.rootDir)
        new ProcessBuilder("sh", "mkconfig.sh").directory(FileManager.currentProject.rootDir) .start .waitFor

        println("done.")

        mkPage("Generating Config", <pre>{scala.io.Source.fromFile(new java.io.File(FileManager.currentProject.linuxDir,".config")).getLines().mkString("\n")}</pre>)
    }
}
