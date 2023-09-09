package api2

import api2.dtos.ReajusteSalarioDTO
import api2.traits.ExceptionHandlers

class ReajusteSalarioController implements ExceptionHandlers {

    static responseFormats = ["json"]
    static defaultAction = "get"
    static allowedMethods = [
            save: "POST",
            list: "GET",
            update: "PUT",
            delete: "DELETE",
            get: "GET"
    ]

    ReajusteSalarioService reajusteSalarioService

    def list() {
        respond reajusteSalarioService.list()
    }

    def save() {
        respond reajusteSalarioService.save()
    }

    def update(Long id) {
        Long reajusteId = params.id ? params.id.toLong() : params.id
        ReajusteSalarioDTO reajusteSalarioDTO = request.JSON
        respond reajusteSalarioService.update(reajusteId, reajusteSalarioDTO)
    }

    def delete() {
        Map retorno = reajusteSalarioService.delete()

        respond(retorno)
    }

    def get(Long id) {
        Long reajusteId = params.id ? params.id.toLong() : params.id
        respond reajusteSalarioService.get(reajusteId)
    }

    // Método adicional para listar reajustes por funcionário
    def listByFuncionario() {
        Long funcionarioId = params.funcionarioId ? params.funcionarioId.toLong() : params.funcionarioId
        respond reajusteSalarioService.getByFuncionario(funcionarioId)

    }
}
