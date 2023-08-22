package api2

import grails.gorm.transactions.Transactional
import api2.ReajusteSalarioDTO
import java.time.LocalDate
import java.time.format.DateTimeFormatter;

@Transactional
class ReajusteSalarioService {

    List<ReajusteSalarioDTO> listarTodos() {
        ReajusteSalario.findAll().collect { ReajusteSalario reajuste ->
            new ReajusteSalarioDTO(
                    id: reajuste.id,
                    dataReajuste:  reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    valorSalario: reajuste.valorSalario,
                    funcionarioId: reajuste.funcionario.id
            )
        }
    }

    ReajusteSalarioDTO salvar(ReajusteSalarioDTO reajusteDTO) {
        Funcionario funcionario = Funcionario.get(reajusteDTO.funcionarioId)
        if (!funcionario) {
            throw new EntidadeNaoEncontradaException("Funcionário com ID ${reajusteDTO.funcionarioId} não encontrado.")
        }
        LocalDate newDate = LocalDate.parse(reajusteDTO.dataReajuste, DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        ReajusteSalario reajuste = new ReajusteSalario(
                dataReajuste: newDate,
                valorSalario: reajusteDTO.valorSalario,
                funcionario: funcionario
        )

        if (!reajuste.save(flush: true)) {
            if (reajuste.errors.hasFieldErrors('funcionario')) {
                throw new ErroDePersistenciaException("Reajuste salarial já existe para o funcionário na data especificada")
            }
            throw new ErroDePersistenciaException("Erro ao salvar o reajuste salarial")
        }
        reajusteDTO.id = reajuste.id
        return reajusteDTO
    }

    ReajusteSalarioDTO atualizar(Long id, ReajusteSalarioDTO reajusteDTO) {
        ReajusteSalario reajuste = ReajusteSalario.get(id)
        if (!reajuste) {
            throw new EntidadeNaoEncontradaException("Reajuste salarial com ID ${id} não encontrado.")
        }

        Funcionario funcionario = Funcionario.get(reajusteDTO.funcionarioId)
        if (!funcionario) {
            throw new EntidadeNaoEncontradaException("Funcionário com ID ${reajusteDTO.funcionarioId} não encontrado.")
        }

        reajuste.dataReajuste = reajusteDTO.dataReajuste
        reajuste.valorSalario = reajusteDTO.valorSalario
        reajuste.funcionario = funcionario
        if (!reajuste.save(flush: true)) {
            throw new ErroDePersistenciaException("Erro ao atualizar o reajuste salarial com ID ${id}.")
        }
        return new ReajusteSalarioDTO(
                id: reajuste.id,
                dataReajuste: reajuste.dataReajuste.format("dd/MM/yyyy"),
                valorSalario: reajuste.valorSalario,
                funcionarioId: reajuste.funcionario.id
        )
    }

    void deletar(Long id) {
        ReajusteSalario reajuste = ReajusteSalario.get(id)
        if (!reajuste) {
            throw new EntidadeNaoEncontradaException("Reajuste salarial com ID ${id} não encontrado.")
        }

        reajuste.delete(flush: true)
    }

    ReajusteSalarioDTO obterPorId(Long id) {
        ReajusteSalario reajuste = ReajusteSalario.get(id)

        return new ReajusteSalarioDTO(
                id: reajuste.id,
                dataReajuste: reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                valorSalario: reajuste.valorSalario,
                funcionarioId: reajuste.funcionario.id
        )
    }

    // Recupera os reajustes de salário pelo ID do funcionário.
    List<ReajusteSalarioDTO> getByFuncionario(Long funcionarioId) {
        ReajusteSalario.findAllByFuncionario(Funcionario.get(funcionarioId)).collect { ReajusteSalario reajuste ->
            new ReajusteSalarioDTO(
                    id: reajuste.id,
                    dataReajuste:  reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    valorSalario: reajuste.valorSalario,
                    funcionarioId: reajuste.funcionario.id
            )
        }
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
