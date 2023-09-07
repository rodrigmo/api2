package api2

import grails.gorm.transactions.Transactional
import api2.CidadeDTO
import grails.web.api.ServletAttributes

@Transactional
class CidadeService implements ServletAttributes {

    List<CidadeDTO> listarTodos() {
        Cidade.findAll().collect { Cidade cidade ->
            new CidadeDTO(id: cidade.id, nome: cidade.nome)
        }
    }

    CidadeDTO salvar(CidadeDTO cidadeDTO) {
        Cidade cidade = new Cidade(nome: cidadeDTO.nome)
        if (!cidade.save(flush: true)) {
            throw new ErroDePersistenciaException("Erro ao salvar a cidade.")
        }
        cidadeDTO.id = cidade.id
        return cidadeDTO
    }

    CidadeDTO atualizar(Long id, CidadeDTO cidadeDTO) {
        Cidade cidade = Cidade.get(id)
        if (!cidade) {
            throw new EntidadeNaoEncontradaException("Cidade com ID ${id} não encontrada.")
        }

        cidade.nome = cidadeDTO.nome
        if (!cidade.save(flush: true)) {
            throw new ErroDePersistenciaException("Erro ao atualizar a cidade com ID ${id}.")
        }
        return new CidadeDTO(id: cidade.id, nome: cidade.nome)
    }

    void deletar(Long id) {
        Cidade cidade = Cidade.get(id)
        if (!cidade) {
            throw new EntidadeNaoEncontradaException("Cidade com ID ${id} não encontrada.")
        }
        cidade.delete(flush: true)
    }

    CidadeDTO obterPorId(Long id) {
        Cidade cidade = Cidade.get(id)
        if (!cidade) {
            throw new EntidadeNaoEncontradaException("Cidade com ID ${id} não encontrada.")
        }

        return new CidadeDTO(id: cidade.id, nome: cidade.nome)
    }

    // Exceção para quando uma entidade não é encontrada
    class EntidadeNaoEncontradaException extends RuntimeException {
        EntidadeNaoEncontradaException(String message) {
            super(message)
        }
    }

    // Exceção para quando ocorre um erro ao salvar ou atualizar uma entidade
    class ErroDePersistenciaException extends RuntimeException {
        ErroDePersistenciaException(String message) {
            super(message)
        }
    }

}
