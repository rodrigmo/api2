package api2

import api2.dtos.FuncionarioDTO
import api2.traits.ExceptionHandlers

class FuncionarioController implements ExceptionHandlers {

    static responseFormats = ["json"]
    static defaultAction = "get"
    static allowedMethods = [
            save: "POST",
            list: "GET",
            update: "PUT",
            delete: "DELETE",
            get: "GET"
    ]

    FuncionarioService funcionarioService

    def list() {
        respond funcionarioService.list()
    }

    def save() {
        FuncionarioDTO funcDTO = new FuncionarioDTO()
        funcDTO.cidadeId = request.JSON.cidadeId ? request.JSON.cidadeId.toLong() : request.JSON.cidadeId
        funcDTO.nome = request.JSON.nome

        respond funcionarioService.save(funcDTO)
    }

    def update(Long id) {
        FuncionarioDTO funcDTO = new FuncionarioDTO()
        funcDTO.cidadeId = request.JSON.cidadeId ? request.JSON.cidadeId.toLong() : request.JSON.cidadeId
        funcDTO.nome = request.JSON.nome

        respond funcionarioService.update(id, funcDTO)
    }

    def delete(Long id) {
        respond funcionarioService.delete(id)
    }

    def get(Long id) {
        respond funcionarioService.get(params.id.toLong())
    }
}
