package api2

import grails.gorm.transactions.Transactional
import api2.dtos.ReajusteSalarioDTO
import grails.web.api.ServletAttributes
import javassist.NotFoundException

import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException;

@Transactional
class ReajusteSalarioService implements ServletAttributes {

    List<ReajusteSalarioDTO> list() {
        ReajusteSalario.findAll().collect { ReajusteSalario reajuste ->
            new ReajusteSalarioDTO(
                    id: reajuste.id,
                    dataReajuste:  reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    valorSalario: reajuste.valorSalario,
                    funcionarioId: reajuste.funcionario.id
            )
        }
    }

    ReajusteSalarioDTO save() {
        if (!request.JSON.funcionarioId) {
            throw new NotFoundException("Funcionário inválido ou não fornecido.")
        }

        Long funcId = request.JSON.funcionarioId ? request.JSON.funcionarioId.toLong() : request.JSON.funcionarioId
        Funcionario funcionario = Funcionario.get(funcId)
        if (!funcionario) {
            throw new NotFoundException("Funcionário com ID ${funcId} não encontrado.")
        }
        if (!request.JSON.dataReajuste) {
            throw new DateTimeException("Data do reajuste inválida ou não informada.")
        }
        LocalDate newDate
        try {
            newDate = LocalDate.parse(request.JSON.dataReajuste, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (Exception e) {
            throw new DateTimeException("Data do reajuste inválida ou não informada.")
        }

        ReajusteSalario reajuste = new ReajusteSalario(
                dataReajuste: newDate,
                valorSalario: request.JSON.valorSalario,
                funcionario: funcionario
        )

        if (!reajuste.save(flush: true)) {
            if (reajuste.errors.hasFieldErrors('funcionario')) {
                throw new Exception("Reajuste salarial já existe para o funcionário na data especificada")
            }
            throw new Exception("Erro ao salvar o reajuste salarial")
        }

        ReajusteSalarioDTO reajusteDTO = new ReajusteSalarioDTO()
        reajusteDTO.dataReajuste = reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        reajusteDTO.id = reajuste.id
        reajusteDTO.valorSalario = reajuste.valorSalario
        reajusteDTO.funcionarioId = reajuste.funcionarioId

        return reajusteDTO
    }

    ReajusteSalarioDTO update(Long id, ReajusteSalarioDTO reajusteDTO) {
        ReajusteSalario reajuste = ReajusteSalario.get(id)
        if (!reajuste) {
            throw new NotFoundException("Reajuste salarial com ID ${id} não encontrado.")
        }

        Funcionario funcionario = Funcionario.get(reajusteDTO.funcionarioId)
        if (!funcionario) {
            throw new NotFoundException("Funcionário com ID ${reajusteDTO.funcionarioId} não encontrado.")
        }

        reajuste.dataReajuste = reajusteDTO.dataReajuste
        reajuste.valorSalario = reajusteDTO.valorSalario
        reajuste.funcionario = funcionario
        if (!reajuste.save(flush: true)) {
            throw new Exception("Erro ao atualizar o reajuste salarial com ID ${id}.")
        }
        return new ReajusteSalarioDTO(
                id: reajuste.id,
                dataReajuste: reajuste.dataReajuste.format("dd/MM/yyyy"),
                valorSalario: reajuste.valorSalario,
                funcionarioId: reajuste.funcionario.id
        )
    }

    Map delete() {
        Map retorno = [success: true]

        Long reajusteId = params.id ? params.id.toLong() : params.id
        if (!reajusteId) {
            throw new NullPointerException("Reajuste ID não fornecido ou inválido.")
        }
        ReajusteSalario reajuste = ReajusteSalario.get(reajusteId)
        if (!reajuste) {
            throw new NotFoundException("Reajuste salarial com ID ${reajusteId} não encontrado.")
        }

        reajuste.delete(flush: true)
        return retorno
    }

    ReajusteSalarioDTO get(Long id) {
        ReajusteSalario reajuste = ReajusteSalario.findById(id)
        if (!reajuste) {
            throw new NotFoundException("Reajuste com ID ${id} não encontrado.")
        }

        return new ReajusteSalarioDTO(
                id: reajuste.id,
                dataReajuste: reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                valorSalario: reajuste.valorSalario,
                funcionarioId: reajuste.funcionario.id
        )
    }

    // Recupera os reajustes de salário pelo ID do funcionário.
    List<ReajusteSalarioDTO> getByFuncionario(Long funcionarioId) {

        List<ReajusteSalario> reajustes = ReajusteSalario.findAllByFuncionario(Funcionario.get(funcionarioId))
        if (!reajustes) {
            throw new NotFoundException("Reajustes para o funcinário ${funcionarioId} não encontrados.")
        }

        return ReajusteSalario.findAllByFuncionario(Funcionario.get(funcionarioId)).collect { ReajusteSalario reajuste ->
            new ReajusteSalarioDTO(
                    id: reajuste.id,
                    dataReajuste:  reajuste.dataReajuste.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    valorSalario: reajuste.valorSalario,
                    funcionarioId: reajuste.funcionario.id
            )
        }
    }

}
