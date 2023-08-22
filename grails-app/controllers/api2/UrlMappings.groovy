package api2

class UrlMappings {

    static mappings = {
        "/"(controller: "application", action: "index")
        "/$controller/$action?/$id?"()
        get "/reajusteSalario/funcionario/$funcionarioId"(controller: "reajusteSalario", action: "listByFuncionario")

        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
