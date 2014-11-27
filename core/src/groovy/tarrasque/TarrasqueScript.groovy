package groovy.tarrasque

import groovy.transform.CompileStatic
import static groovy.io.FileType.FILES

class TarrasqueScript{
	static public actions = [:]
	private GroovyScriptEngine gse

	TarrasqueScript(GroovyScriptEngine gse, encode){
		this.gse = gse
		this.gse.config.sourceEncoding = encode
	}

	void buildActions(scriptsPath){
		println scriptsPath
		def dir = new File(scriptsPath)
		dir.eachDirRecurse() { subDir ->
			subDir.eachFileMatch(~/.*.groovy/) { file ->  
				gse.run(file.getPath(), getNewBinding());
    	} 
		}
	}

	//@CompileStatic
	public static void createAction(url = null, block = null){
		if(url && block)
			actions[url] = block
	}

	private Binding getNewBinding(){
		def get = post = delete = put = {url, block ->
			createAction(url, block as Closure)
		}

		Binding binding = new Binding();
		binding.setVariable("get", get);
		binding.setVariable("post", post);
		binding.setVariable("delete", delete);
		binding.setVariable("put", put);

		return binding
	}
}