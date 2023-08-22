package api2

class Cidade {
    String nome

    static hasMany = [funcionarios: Funcionario]

    static constraints = {
        nome(blank: false, nullable: false, maxSize: 50)
    }

    static mapping = {
        id generator: 'increment'
        version false
        table 'cidades'
    }
}
