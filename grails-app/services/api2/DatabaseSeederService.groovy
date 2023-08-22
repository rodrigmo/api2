package api2

import grails.gorm.transactions.Transactional

import java.time.LocalDate

@Transactional
class DatabaseSeederService {

    void seedData() {
        if (!Cidade.count()) {
            println("Inicializando Cidades no Mockup")
            def cidade1 = new Cidade(nome: "Cidade1").save()
            def cidade2 = new Cidade(nome: "Cidade2").save()
            def cidade3 = new Cidade(nome: "Cidade3").save()
            println("Finalizada inicialização de Cidades no Mockup")

            if (!Funcionario.count()) {
                println("Inicializando Funcionarios no Mockup")
                def funcionario1 = new Funcionario(nome: "Funcionario1", cidade: cidade1).save()
                def funcionario2 = new Funcionario(nome: "Funcionario2", cidade: cidade2).save()
                def funcionario3 = new Funcionario(nome: "Funcionario3", cidade: cidade3).save()
                println("Finalizada inicialização de Funcionarios no Mockup")

                if (!ReajusteSalario.count()) {
                    println("Inicializando ReajusteSalario no Mockup")
                    new ReajusteSalario(dataReajuste: LocalDate.now(), valorSalario: new BigDecimal("1000.50"), funcionario: funcionario1).save()
                    new ReajusteSalario(dataReajuste: LocalDate.now(), valorSalario: new BigDecimal("2000.50"), funcionario: funcionario2).save()
                    new ReajusteSalario(dataReajuste: LocalDate.now(), valorSalario: new BigDecimal("3000.50"), funcionario: funcionario3).save()
                    println("Finalizada inicialização de ReajusteSalarios no Mockup")
                }
            }
        }
    }
}
