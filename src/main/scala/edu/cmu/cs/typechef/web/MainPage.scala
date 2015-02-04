package edu.cmu.cs.typechef.web


trait MainPage extends Utils {
    private def renderProjectList() = <div>Current project: {FileManager.currentProject.projectFolder} -- Switch: {(for (p<-FileManager.projects) yield <span><a href={"?project="+p.projectFolder}>{p.projectFolder}</a> </span>)}</div>

    private def renderFileListButtons(f: String) =  <div><a href={"/details/"+urlEncode(f)}>details</a>
        <a href={"/reset/"+urlEncode(f)}>reset</a></div>

    def renderFileList(list: List[(String, Boolean, String)])  = mkPage(FileManager.currentProject.projectFolder, <div>
        <p>
            <a href="?">all files</a> <a href="?failed=true">failed files only</a>  <a href="?refresh=true">refresh list</a>
        </p>
        <p>{renderProjectList()}</p>
        <p>Files:


            <table>
                <tr><th>File</th><th>Status</th><th>Actions</th></tr>{for (status <- list) yield
                <tr id="my_tr">
                    <td>{status._1}</td> <td>{status._3}</td> <td>{renderFileListButtons(status._1)}</td>
                </tr>}
            </table>
        </p>
    </div>                                                                                                        )
}
