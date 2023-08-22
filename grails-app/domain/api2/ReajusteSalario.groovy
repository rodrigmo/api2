package api2

import java.time.LocalDate

class ReajusteSalario {
    LocalDate dataReajuste
    BigDecimal valorSalario
    Funcionario funcionario

    static belongsTo = [funcionario: Funcionario]

    static constraints = {
        funcionario unique: 'dataReajuste'
        dataReajuste(blank: false, nullable: false)
        valorSalario(blank: false, nullable: false, scale: 2, precision: 6)
        funcionario(blank: false, nullable: false)
    }

    static mapping = {
        id generator: 'increment'
        version false
        table 'reajustes_salario'
    }
}
