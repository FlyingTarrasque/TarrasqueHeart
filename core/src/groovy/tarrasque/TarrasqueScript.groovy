package groovy.tarrasque

class TarrasqueScript{
	static public actions = [:]
	private GroovyScriptEngine gse

	TarrasqueScript(GroovyScriptEngine gse){
		this.gse = gse
		this.gse.config.sourceEncoding = "UTF-8"
	}

	void buildActions(scriptsPath){
		def dir = new File(scriptsPath)
		dir.eachFileRecurse (FileType.FILES) { file ->
   		bindScript = getNewBinding
			gse.run(file.name, getNewBinding);
		}
	}

	private void createAction(){
		actions[url] = block
	}

	private getNewBinding(){
		def get = post = delete = put = {url, block ->
			createAction(url, block)
		}

		Binding binding = new Binding();
		binding.setVariable("get", get);
		binding.setVariable("post", post);
		binding.setVariable("delete", delete);
		binding.setVariable("put", put);

		return binding
	}

	
}