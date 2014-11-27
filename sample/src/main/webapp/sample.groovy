get("/home"){params ->
	println "chamada da home"
	println params
	return "blastoise"
}