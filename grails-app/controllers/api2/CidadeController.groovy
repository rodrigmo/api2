package api2

import api2.traits.ExceptionHandlers
import api2.dtos.CidadeDTO

class CidadeController implements ExceptionHandlers {

    static responseFormats = ["json"]
    static defaultAction = "get"
    static allowedMethods = [
            save: "POST",
            list: "GET",
            update: "PUT",
            delete: "DELETE",
            get: "GET"
    ]

    CidadeService cidadeService

    def list() {
        respond cidadeService.list()
    }

    def save() {
        CidadeDTO cidadeDTO = request.JSON
        respond cidadeService.save(cidadeDTO)
    }

    def update(Long id) {
        CidadeDTO cidadeDTO = request.JSON
        respond cidadeService.update(id, cidadeDTO)
    }

    def delete(Long id) {
        Map retorno = cidadeService.delete(id)
        respond(retorno)
    }

    def get() {
        Long cidadeId = Long.parseLong(params.id)
        CidadeDTO cidadeDTO = cidadeService.obterPorId(cidadeId)
        respond cidadeDTO
    }
}
