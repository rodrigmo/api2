package api2

import grails.gorm.transactions.Transactional
import api2.dtos.FuncionarioDTO
import grails.web.api.ServletAttributes
import javassist.NotFoundException

@Transactional
class FuncionarioService implements ServletAttributes {

    List<FuncionarioDTO> list() {
        Funcionario.findAll().collect { Funcionario funcionario ->
            new FuncionarioDTO(id: funcionario.id, nome: funcionario.nome, cidadeId: funcionario.cidade.id)
        }
    }

    FuncionarioDTO save(FuncionarioDTO funcionarioDTO) {
        if (!funcionarioDTO.cidadeId) {
            throw new NullPointerException("Cidade ID não fornecido.")
        }
        if (!funcionarioDTO.nome) {
            throw new NullPointerException("Nome não fornecido.")
        }

        Cidade cidade = Cidade.get(funcionarioDTO.cidadeId)

        if (!cidade) {
            throw new NotFoundException("Cidade com ID ${funcionarioDTO.cidadeId} não encontrada.")
        }

        Funcionario funcionario = new Funcionario(nome: funcionarioDTO.nome, cidade: cidade)
        if (!funcionario.save(flush: true)) {
            throw new Exception("Erro ao salvar o funcionário.")
        }
        funcionarioDTO.id = funcionario.id
        return funcionarioDTO
    }

    FuncionarioDTO update(Long id, FuncionarioDTO funcionarioDTO) {
        if (!funcionarioDTO.cidadeId) {
            throw new NullPointerException("Cidade ID não fornecido.")
        }
        if (!funcionarioDTO.nome) {
            throw new NullPointerException("Nome não fornecido.")
        }

        Funcionario funcionario = Funcionario.get(id)
        if (!funcionario) {
            throw new NotFoundException("Funcionário com ID ${id} não encontrado.")
        }

        Cidade cidade = Cidade.get(funcionarioDTO.cidadeId)

        funcionario.nome = funcionarioDTO.nome
        funcionario.cidade = cidade
        if (!funcionario.save(flush: true)) {
            throw new Exception("Erro ao atualizar o funcionário com ID ${id}.")
        }
        return new FuncionarioDTO(id: funcionario.id, nome: funcionario.nome, cidadeId: funcionario.cidade.id)
    }

    Map delete(Long id) {
        Map retorno = [success: true]

        Funcionario funcionario = Funcionario.findById(id)
        if (!funcionario) {
            throw new NotFoundException("Funcionário com ID ${id} não encontrado.")
        }
        funcionario.delete(flush: true)
        return retorno
    }

    FuncionarioDTO get(Long id) {
        Funcionario funcionario = Funcionario.get(id)
        if (!funcionario) {
            throw new NotFoundException("Funcionário com ID ${id} não encontrado.")
        }

        return new FuncionarioDTO(
                id: funcionario.id,
                nome: funcionario.nome,
                cidadeId: funcionario.cidade.id
        )
    }

}
