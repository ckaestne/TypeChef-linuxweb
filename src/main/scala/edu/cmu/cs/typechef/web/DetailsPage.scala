package edu.cmu.cs.typechef.web

import scala.xml.NodeSeq

/**
 * Created by ckaestne on 2/4/15.
 */
trait DetailsPage extends Utils {


    def renderFileDetails(filename: String): NodeSeq = {

        val filepc = FileManager.getFilePC(filename)
        val errors = FileManager.getErrors(filename)
        val log = FileManager.getDbgOutput(filename)
        val errorLog = FileManager.getErrorOutput(filename)
        val comment = FileManager.getComments(filename)

        mkPage("Details: "+filename, <div>
            <h1>Analysis details:
                {filename}
            </h1>

            <div>
                <a href={"/reset/" + urlEncode(filename)}>reset</a>
            </div>

            <h2>File presence condition</h2>

            <div>
                <span>
                    {filepc.toTextExpr}
                </span>
                (
                {renderDebugPCButton(filepc)}
                )</div>

            <h2>Comments</h2>

            <form action={"/updateComment/" + urlEncode(filename)} method="post">
                <div>
                    <textarea name="comment">{comment}</textarea>
                    <input value="Update" type="submit" name="submit"/>
                    <input value="Delete" type="submit" name="delete"/>
                </div>
            </form>

            <h2>Errors</h2>
            <table>
                <tr>
                    <th>Msg</th> <th>Condition</th> <th>Severity</th> <th>Location</th>
                </tr>{for (err <- errors)
                        yield <tr>
                                <td>
                                    {err._1}
                                </td>
                                <td>
                                    {err._2.toString}<br/>{renderDebugPCButton(filepc and err._2)}
                                </td>
                                <td>
                                    {err._3}
                                </td>
                                <td>
                                    {err._4._1 + ":" + err._4._2 + ":" + err._4._3}
                                </td>
                            </tr>}
            </table>

            <h2>Log</h2>

            <pre>
                {log}
            </pre>

            <h2>Error Log</h2>

            <pre>
                {errorLog}
            </pre>
        </div>)
    }
}
