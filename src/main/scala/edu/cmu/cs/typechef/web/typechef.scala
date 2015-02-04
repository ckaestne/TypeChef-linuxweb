package edu.cmu.cs.typechef.web

import java.net.URLEncoder

import de.fosd.typechef.featureexpr.FeatureExpr

import scala.xml.NodeSeq

class typechef extends TypecheflinuxwebStack with MainPage with DetailsPage with PCPage {



    get("/") {
        if (params.get("refresh").isDefined)
            FileManager.resetCache()
        val isFilter = params.get("failed").isDefined
        params.get("project").map(s=>FileManager.setProject(s))
        val status = FileManager.fileStatusList.filter(s=> !s._2 || !isFilter)

        renderFileList(status)
    }


    get("/details/:file") {
        params.get("file").map(renderFileDetails).getOrElse(<div>Unknown file</div>)
    }

    get("/pc/:pc") {
        renderPC(params("pc").replace("+"," "))
    }

    get("/genConfig/:pc") {
        generateConfig(params("pc").replace("+"," "))
    }

    post("/updateComment/:file") {
        val s = params("file")
        if (params.get("submit").isDefined)
            FileManager.setComments(s, params.getOrElse("comment",""))
        if (params.get("delete").isDefined)
            FileManager.deleteComments(s)

        redirect("/details/"+urlEncode(s))
    }

    get("/reset/:file") {
        val s = params("file")
        FileManager.resetFile(s)
        redirect("/details/"+urlEncode(s))
    }





}
