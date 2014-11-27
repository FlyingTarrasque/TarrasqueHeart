package groovy.tarrasque

import groovy.servlet.GroovyServlet
import groovy.servlet.ServletBinding
import groovy.transform.CompileStatic
import groovy.lang.Binding
import groovy.util.GroovyScriptEngine

import javax.servlet.ServletConfig
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TarrasqueServletDispatcher extends GroovyServlet {

  private TarrasqueScript tarrasqueScript

  @Override
  @CompileStatic
  void init(ServletConfig config) {
    super.init(config)
    tarrasqueScript = new TarrasqueScript(createGroovyScriptEngine())        

    tarrasqueScript.buildActions(servletContext.getResource("/").toExternalForm())
  }

  @Override
  @CompileStatic
  protected void setVariables(ServletBinding binding) {
        // 
  }

  @Override
  @CompileStatic
  void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.contentType = "text/html; charset=utf-8"

    def result = doService(request, response)
  }

  /**
   * Handle web requests to the GroovyServlet
   */
   @CompileStatic
   private doService(HttpServletRequest request, HttpServletResponse response) throws IOException {

    final ServletBinding binding = [request, response, servletContext]
    setVariables(binding)

    String scriptUri = super.getScriptUri(request)
    def result = null

    result
  }

  @CompileStatic
  private executeGroovlet(String scriptUri, ServletBinding binding) {
    //Closures executer based on route
    gse.run(scriptUri, binding)
  }

}