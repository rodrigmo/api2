package api2

import api2.CidadeDTO
import grails.rest.*
import org.grails.web.json.JSONException
import org.springframework.web.bind.annotation.RestController

@RestController("/cidade")
class CidadeController {

    CidadeService cidadeService

    def list() {
        if (request.method != 'GET') {
            render status: 405, text: 'Método não permitido'
            return
        }

        respond cidadeService.listarTodos()
    }

    def save() {
        if (request.method != 'POST') {
            render status: 405, text: 'Método não permitido'
            return
        }

        try {
            if (!request.JSON.nome) {
                render status: 404, text: "Nome da cidade não fornecido."
                return
            }
            CidadeDTO cidadeDTO = request.JSON
            respond cidadeService.salvar(cidadeDTO)
        } catch(NumberFormatException e) {
            render status: 400, text: "ID de cidade fornecido é inválido."
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
            CidadeDTO cidadeDTO = request.JSON
            if (!cidadeDTO.nome) {
                render status: 404, text: "Nome da cidade não fornecido."
                return
            }
            respond cidadeService.atualizar(id, cidadeDTO)
        } catch (CidadeService.EntidadeNaoEncontradaException e) {
            render status: 404, text: "Cidade com ID ${params.id} não encontrada."
        } catch(NumberFormatException e) {
            render status: 400, text: "ID de cidade fornecido é inválido."
        } catch (Exception e) {
            render status: 404, text: "Dados incorretos para update."
        }
    }

    def delete(Long id) {
        if (request.method != 'DELETE') {
            render status: 405, text: 'Método não permitido'
            return
        }

        try {
            cidadeService.deletar(id)
            render status: 200, text: 'Cidade excluída com sucesso.'
        } catch (CidadeService.EntidadeNaoEncontradaException e) {
            render status: 404, text: "Cidade com ID ${params.id} não encontrada."
        } catch(NumberFormatException e) {
            render status: 400, text: "ID de cidade fornecido é inválido."
        } catch (Exception e) {
            render status: 404, text: "Dados incorretos para delete."
        }
    }

    def get() {
        if (request.method != 'GET') {
            render status: 405, text: 'Método não permitido'
            return
        }

        try {
            Long cidadeId = Long.parseLong(params.id)
            CidadeDTO cidadeDTO = cidadeService.obterPorId(cidadeId)
            if (!cidadeDTO) {
                render status: 404, text: "Cidade com ID ${cidadeId} não encontrada."
                return
            }
            respond cidadeDTO
        } catch (CidadeService.EntidadeNaoEncontradaException e) {
            render status: 404, text: "Cidade com ID ${params.id} não encontrado."
        } catch(NumberFormatException e) {
            render status: 400, text: "ID da cidade fornecido é inválido."
        }
    }
}
