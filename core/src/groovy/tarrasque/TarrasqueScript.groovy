package groovy.tarrasque

import groovy.transform.CompileStatic
import static groovy.io.FileType.*

class TarrasqueScript{
	static public actions = [:]
	private GroovyScriptEngine gse

	TarrasqueScript(GroovyScriptEngine gse, encode){
		this.gse = gse
		this.gse.config.sourceEncoding = encode
	}

	void buildActions(scriptsPath){
		println "Root path: $scriptsPath"
		def dir = new File(scriptsPath).getParentFile()
		dir.eachDirRecurse() { subDir ->
			println "Folder: $subDir.path"
			subDir.eachFile{ file ->  
				if(file.exists() && file.path.endsWith(".groovy")){
					println "Iniciando o script $file.name"
					gse.run(file.name, getNewBinding());	
				}
    	} 
		}
	}

	//@CompileStatic
	public static void createAction(url = null, block = null){
		if(url && block)
			actions[url] = block
	}

	private Binding getNewBinding(){
		//TODO: Serparar closures baseado em seus verbos
		def actionCallback = {url, block ->
			createAction(url, block as Closure)
		}

		Binding binding = new Binding();
		binding.setVariable("get", actionCallback);
		binding.setVariable("post", actionCallback);
		binding.setVariable("delete", actionCallback);
		binding.setVariable("put", actionCallback);

		return binding
	}
}