package api2.traits

import grails.artefact.controller.RestResponder
import javassist.NotFoundException
import org.grails.web.converters.exceptions.ConverterException

import java.lang.reflect.InvocationTargetException
import java.time.format.DateTimeParseException

trait ExceptionHandlers implements RestResponder {

    def handleNullPointerException(NullPointerException e) {
        respond([message: e.getMessage() ?: "NullPointerException"], status: 400)
    }

    def handleNotFoundException(NotFoundException e) {
        respond([message: e.getMessage()], status: 404)
    }

    def handleNumberFormatException(NumberFormatException e) {
        respond([message: "Formato invalido"], status: 400)
    }

    def handleDateTimeParseException(DateTimeParseException e) {
        respond([message: "Formato invalido de data invalida"], status: 420)
    }

    def handleException(Exception e) {
        if (e.getMessage() == "Error parsing JSON") {
            respond([message: "Dados necessários não informados"], status: 400)
        } else {
            respond([message: "Algo de errado não deu certo: "+e.getMessage()], status: 400)
        }
    }

}