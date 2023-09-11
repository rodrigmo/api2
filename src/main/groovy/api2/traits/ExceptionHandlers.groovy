package api2.traits

import grails.artefact.controller.RestResponder
import javassist.NotFoundException
import org.grails.web.converters.exceptions.ConverterException

import java.lang.reflect.InvocationTargetException
import java.time.DateTimeException
import java.time.format.DateTimeParseException

trait ExceptionHandlers implements RestResponder {

    def handleNullPointerException(NullPointerException e) {
        respond([message: e.getMessage() ?: "NullPointerException"], status: 400)
    }

    def handleNotFoundException(NotFoundException e) {
        respond([message: e.getMessage()], status: 404)
    }

    def handleNumberFormatException(NumberFormatException e) {
        respond([message: "Parâmetro fornecido com formato invalido ou não fornecido"], status: 400)
    }

    def handleDateTimeException(DateTimeException e) {
        respond([message: e.getMessage() ?: "Formato de data inválida"], status: 400)
    }

//    def handleDateTimeParseException(DateTimeParseException e) {
//        respond([message: e.getMessage() ?: "Formato de data inválida"], status: 400)
//    }

    def handleException(Exception e) {
        if (e.getMessage() == "Error parsing JSON") {
            respond([message: "Dados necessários não informados"], status: 400)
        } else if (e.getMessage() == "Data do reajuste inválida ou não informada.") {
            respond([message: "Data do reajuste inválida ou não informada."], status: 400)
        } else if (e.getMessage() == "Reajuste salarial já existe para o funcionário na data especificada") {
            respond([message: "Reajuste salarial já existe para o funcionário na data especificada"], status: 400)
        } else {
            respond([message: "Algo de errado não deu certo: "+e.getMessage()], status: 400)
        }
    }

}