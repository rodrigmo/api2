package api2

import api2.FuncionarioDTO
import grails.rest.*
import org.springframework.web.bind.annotation.RestController

@RestController("/funcionario")
class FuncionarioController {

    FuncionarioService funcionarioService

    def list() {
        if (request.method != 'GET') {
            render status: 405, text: 'Método não permitido'
            return
        }

        respond funcionarioService.listarTodos()
    }

    def save() {
        if (request.method != 'POST') {
            render status: 405, text: 'Método não permitido'
            return
        }

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
        if (request.method != 'PUT') {
            render status: 405, text: 'Método não permitido'
            return
        }

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
        if (request.method != 'DELETE') {
            render status: 405, text: 'Método não permitido'
            return
        }
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
        if (request.method != 'GET') {
            render status: 405, text: 'Método não permitido'
            return
        }

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
