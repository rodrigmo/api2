package api2

import api2.ReajusteSalarioDTO
import grails.rest.*
import org.springframework.web.bind.annotation.RestController

import java.time.format.DateTimeParseException

@RestController("/reajusteSalario")
class ReajusteSalarioController {

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
        respond reajusteSalarioService.listarTodos()
    }

    def save() {
        try {
            if (!request.JSON.dataReajuste) {
                render status: 400, text: "Data do reajuste não fornecida."
                return
            }
            if (!request.JSON.funcionarioId) {
                render status: 400, text: "Funcionário para o reajuste não fornecido."
                return
            }
            if (!request.JSON.valorSalario) {
                render status: 400, text: "Valor do reajuste não fornecido."
                return
            }
            Long funcionarioId = request.JSON.funcionarioId.toLong()
            ReajusteSalarioDTO reajusteSalarioDTO = request.JSON
            respond reajusteSalarioService.salvar(reajusteSalarioDTO)
        } catch (ReajusteSalarioService.EntidadeNaoEncontradaException e) {
            render status: 404, text: "Funcionário com ID ${request.JSON.funcionarioId} não encontrado."
        } catch(NumberFormatException e) {
            render status: 400, text: "ID de funcionário fornecido é inválido."
        } catch (DateTimeParseException d) {
            render status: 400, text: "Data deve ser enviada no formato dd/MM/yyyy: ${d.message}."
        } catch (Exception e) {
            render status: 400, text: "Dados incorretos: ${e.message}."
        }
    }

    def update(Long id) {
        try {
            Long reajusteId = params.id.toLong()
            ReajusteSalarioDTO reajusteSalarioDTO = request.JSON
            respond reajusteSalarioService.atualizar(id, reajusteSalarioDTO)
        } catch(NumberFormatException e) {
            render status: 400, text: "ID inválido fornecido."
        } catch (NullPointerException n) {
            render status: 404, text: "ID fornecido não localizado."
        }
    }

    def delete(Long id) {
        try {
            Long reajusteId = params.id.toLong()
            reajusteSalarioService.deletar(id)
            render status: 200, text: 'Reajuste excluído com sucesso.'
        } catch (ReajusteSalarioService.EntidadeNaoEncontradaException e) {
            render status: 404, text: "Reajuste com ID ${params.id} não encontrado."
        } catch(NumberFormatException e) {
            render status: 400, text: "ID fornecido é inválido."
        } catch (Exception e) {
            render status: 404, text: "Dados incorretos para delete."
        }
    }

    def get(Long id) {
        try {
            Long reajusteId = params.id.toLong()
            respond reajusteSalarioService.obterPorId(id)
        } catch(NumberFormatException e) {
            render status: 400, text: "ID inválido fornecido."
        } catch (NullPointerException n) {
            render status: 404, text: "ID fornecido não localizado."
        }
    }

    // Método adicional para listar reajustes por funcionário
    def listByFuncionario() {
        try {
            def funcionarioId = params.funcionarioId.toLong()
            respond reajusteSalarioService.getByFuncionario(funcionarioId)
        } catch(NumberFormatException e) {
            render status: 400, text: "ID inválido fornecido."
        } catch (NullPointerException n) {
            render status: 404, text: "ID fornecido não localizado."
        }
    }
}
