package api2

import grails.gorm.transactions.Transactional
import api2.dtos.CidadeDTO
import grails.web.api.ServletAttributes
import javassist.NotFoundException
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class CidadeService implements ServletAttributes {

    List<CidadeDTO> list() {
        Cidade.findAll().collect { Cidade cidade ->
            new CidadeDTO(id: cidade.id, nome: cidade.nome)
        }
    }

    CidadeDTO save(CidadeDTO cidadeDTO) {
        if (!cidadeDTO.nome) {
            throw new NullPointerException("Nome da cidade não fornecido.")
        }
        Cidade cidade = new Cidade(nome: cidadeDTO.nome)
        if (!cidade.save(flush: true)) {
            throw new Exception("Erro ao salvar a cidade.")
        }
        cidadeDTO.id = cidade.id
        return cidadeDTO
    }

    CidadeDTO update(Long id, CidadeDTO cidadeDTO) {
        Cidade cidade = Cidade.get(id)
        if (!cidade) {
            throw new NotFoundException("Cidade com ID ${id} não encontrada.")
        }
        if (!cidadeDTO.nome) {
            throw new NullPointerException("Nome da cidade não fornecido.")
        }
        cidade.nome = cidadeDTO.nome
        if (!cidade.save(flush: true)) {
            throw new Exception("Erro ao atualizar a cidade com ID ${id}.")
        }
        return new CidadeDTO(id: cidade.id, nome: cidade.nome)
    }

    Map delete(Long id) {
        Map retorno = [success: true]

        Cidade cidade = Cidade.findById(id)

        if (cidade) {
            try {
                cidade.delete(flush: true)
            } catch (DataIntegrityViolationException e) {
                retorno.success = false
                retorno.message = "Registro associado para um funcionario."
                retorno.error = e.getMessage()
            }
        } else {
            throw new NotFoundException("Não encontrada cidade para ${id}")
        }
        return retorno
    }

    CidadeDTO obterPorId(Long id) {
        Cidade cidade = Cidade.get(id)
        if (!cidade) {
            throw new NotFoundException("Cidade com ID ${id} não encontrada.")
        }

        return new CidadeDTO(id: cidade.id, nome: cidade.nome)
    }
}
