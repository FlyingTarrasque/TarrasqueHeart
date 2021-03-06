package groovy.tarrasque

import groovy.servlet.GroovyServlet
import groovy.servlet.ServletBinding
import groovy.transform.CompileStatic
import groovy.lang.Binding
import groovy.util.GroovyScriptEngine
import groovy.util.XmlSlurper
import groovy.json.JsonSlurper

import javax.servlet.ServletConfig
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TarrasqueServletDispatcher extends GroovyServlet {

  static final String ctJSON = "application/json"
  static final String ctXML  = "application/xml"
  static final String ctHTML = "text/html"
  static final String ctTEXT = "text/plain"
  static final String ctJS   = "text/javascript"
  static final String UTF8 = "UTF-8"

  private TarrasqueScript tarrasqueScript

  @Override
  @CompileStatic
  void init(ServletConfig config) {
    println "Iniciando dispatcher"
    super.init(config)
    tarrasqueScript = new TarrasqueScript(createGroovyScriptEngine(), UTF8)

    def _servletContext = config.getServletContext()
    tarrasqueScript.buildActions(_servletContext.getRealPath("/"))
  }

  //@Override
  //@CompileStatic
  //protected void setVariables(ServletBinding binding) {
        // 
  //}

  @Override
  @CompileStatic
  void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //response.contentType = "text/html; charset=utf-8"

    //Maybe put here a beforeAction
    def result = doService(request, response)
    //Maybe put here a afterAction
  }

  //@CompileStatic
  private static doService(HttpServletRequest request, HttpServletResponse response) throws IOException {
    def data = null
    def ct  = request.contentType?: ctHTML
    if (ct.startsWith(ctJSON)) {
      data = new JsonSlurper().parse(request.inputStream.newReader(UTF8))
    }else if (ct.startsWith(ctXML)) {
      data = new XmlSlurper().parse(request.inputStream.newReader(UTF8))
    }else if (ct.startsWith(ctTEXT)) {
      data = []
      request.inputStream.eachLine(UTF8) { 
        data.push it 
      }
    }

    String route = request.servletPath
    //response.writer.write(route)
    def action = TarrasqueScript.actions[route]
    if(action){
      response.writer.write(action(data))
    }else{
      response.sendError(404, "No matching Routes for $route")
    }
  }
}