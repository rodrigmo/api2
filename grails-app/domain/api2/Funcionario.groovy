package api2

class Funcionario {
    String nome
    Cidade cidade

    static belongsTo = [cidade: Cidade]
    static hasMany = [reajustessalario: ReajusteSalario]

    static constraints = {
        nome(blank: false, nullable: false, maxSize: 50)
        cidade(blank: false, nullable: false)
    }

    static mapping = {
        id generator: 'increment'
        version false
        table 'funcionarios'
    }
}
