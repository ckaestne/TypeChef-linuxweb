package edu.cmu.cs.typechef.web

import java.net.URLEncoder

import de.fosd.typechef.featureexpr.FeatureExpr

import scala.xml.NodeSeq

/**
 * Created by ckaestne on 2/4/15.
 */
trait Utils {
    def urlEncode(s: String): String = URLEncoder.encode(s, "UTF-8")

    def renderDebugPCButton(pc: FeatureExpr) = <a href={"/pc/" + urlEncode(pc.toTextExpr)}>debug pc</a>


    def mkPage(title: String, content: NodeSeq): NodeSeq =
    <html><header><title>{title}</title></header><body>{content}</body></html>

}
