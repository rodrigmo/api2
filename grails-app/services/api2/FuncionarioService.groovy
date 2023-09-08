package api2

import grails.gorm.transactions.Transactional
import api2.dtos.FuncionarioDTO
import grails.web.api.ServletAttributes

@Transactional
class FuncionarioService implements ServletAttributes {

    List<FuncionarioDTO> listarTodos() {
        Funcionario.findAll().collect { Funcionario funcionario ->
            new FuncionarioDTO(id: funcionario.id, nome: funcionario.nome, cidadeId: funcionario.cidade.id)
        }
    }

    FuncionarioDTO salvar(FuncionarioDTO funcionarioDTO) {
        Cidade cidade = Cidade.get(funcionarioDTO.cidadeId)
        if (!cidade) {
            throw new EntidadeNaoEncontradaException("Cidade com ID ${funcionarioDTO.cidadeId} não encontrada.")
        }

        Funcionario funcionario = new Funcionario(nome: funcionarioDTO.nome, cidade: cidade)
        if (!funcionario.save(flush: true)) {
            throw new ErroDePersistenciaException("Erro ao salvar o funcionário.")
        }
        funcionarioDTO.id = funcionario.id
        return funcionarioDTO
    }

    FuncionarioDTO atualizar(Long id, FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = Funcionario.get(id)
        if (!funcionario) {
            throw new EntidadeNaoEncontradaException("Funcionário com ID ${id} não encontrado.")
        }

        Cidade cidade = Cidade.get(funcionarioDTO.cidadeId)
        if (!cidade) {
            throw new EntidadeNaoEncontradaException("Cidade com ID ${funcionarioDTO.cidadeId} não encontrada.")
        }

        funcionario.nome = funcionarioDTO.nome
        funcionario.cidade = cidade
        if (!funcionario.save(flush: true)) {
            throw new ErroDePersistenciaException("Erro ao atualizar o funcionário com ID ${id}.")
        }
        return new FuncionarioDTO(id: funcionario.id, nome: funcionario.nome, cidadeId: funcionario.cidade.id)
    }

    void deletar(Long id) {
        Funcionario funcionario = Funcionario.get(id)
        if (!funcionario) {
            throw new EntidadeNaoEncontradaException("Funcionário com ID ${id} não encontrado.")
        }
        funcionario.delete(flush: true)
    }

    FuncionarioDTO obterPorId(Long id) {
        Funcionario funcionario = Funcionario.get(id)

        return new FuncionarioDTO(
                id: funcionario.id,
                nome: funcionario.nome,
                cidadeId: funcionario.cidade.id
        )
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
