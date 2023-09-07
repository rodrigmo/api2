package api2

import api2.FuncionarioDTO
import grails.rest.*
import org.springframework.web.bind.annotation.RestController

class FuncionarioController {

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
        respond funcionarioService.listarTodos()
    }

    def save() {
        try {
            if (!request.JSON.cidadeId) {
                render status: 404, text: "Cidade ID não fornecido."
                return
            }
            if (!request.JSON.nome) {
                render status: 404, text: "Nome não fornecido."
                return
            }
            Long cidadeId = request.JSON.cidadeId.toLong()
            FuncionarioDTO funcionarioDTO = request.JSON
            respond funcionarioService.salvar(funcionarioDTO)
        } catch (FuncionarioService.EntidadeNaoEncontradaException e) {
            render status: 404, text: "Cidade com ID ${request.JSON.cidadeId} não encontrado."
        } catch(NumberFormatException e) {
            render status: 400, text: "ID da cidade fornecido inválido."
        } catch (Exception e) {
            render status: 404, text: "Dados incorretos."
        }
    }

    def update(Long id) {
        try {
            Long funcionarioId = params.id.toLong()
            if (!request.JSON) {
                render status: 400, text: "Dados incorretos para update."
                return
            }
            FuncionarioDTO funcionarioDTO = funcionarioService.obterPorId(funcionarioId)

            if (request.JSON.cidadeId) {
                funcionarioDTO.setCidadeId(request.JSON.cidadeId)
            }
            if (request.JSON.nome) {
                funcionarioDTO.setNome(request.JSON.nome)
            }

            respond funcionarioService.atualizar(id, funcionarioDTO)

        } catch (FuncionarioService.EntidadeNaoEncontradaException e) {
            render status: 404, text: "Funcionário com ID ${params.id} não encontrado."
        } catch(NumberFormatException e) {
            render status: 400, text: "ID inválido fornecido."
        } catch (NullPointerException n) {
            render status: 404, text: "ID fornecido não localizado."
        } catch (Exception e) {
            render status: 400, text: "Dados incorretos para update."
        }
    }

    def delete(Long id) {
        try {
            funcionarioService.deletar(id)
            render status: 200, text: 'Funcionário excluído com sucesso.'
        } catch (ReajusteSalarioService.EntidadeNaoEncontradaException e) {
            render status: 404, text: "Funionário com ID ${params.id} não encontrado."
        } catch(NumberFormatException e) {
            render status: 400, text: "ID fornecido é inválido."
        } catch (Exception e) {
            render status: 404, text: "Dados incorretos para delete."
        }
    }

    def get(Long id) {
        try {
            Long funcionarioId = params.id.toLong()
            FuncionarioDTO funcionarioDTO = funcionarioService.obterPorId(funcionarioId)
            if (!funcionarioDTO) {
                render status: 404, text: "Funcionário com ID ${funcionarioId} não encontrado."
                return
            }
            respond funcionarioDTO
        } catch(NumberFormatException e) {
            render status: 400, text: "ID inválido fornecido."
        } catch (NullPointerException n) {
            render status: 404, text: "ID fornecido não localizado."
        }
    }
}
