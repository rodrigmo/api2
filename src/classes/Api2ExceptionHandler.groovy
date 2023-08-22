package api2

import grails.plugin.rest.api.exceptions.BadRequestException
import grails.plugin.rest.api.exceptions.NotFoundException
import grails.web.errors.GrailsExceptionResolver

class Api2ExceptionHandler extends GrailsExceptionResolver {

    def handleBadRequestException(BadRequestException e) {
        render(status: 400, text: e.message)
    }

    def handleNotFoundException(NotFoundException e) {
        render(status: 404, text: e.message)
    }

}
