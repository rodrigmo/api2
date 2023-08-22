package api2

import java.time.LocalDate

class BootStrap {
    DatabaseSeederService databaseSeederService

    def init = { servletContext ->
        databaseSeederService.seedData()
    }
    def destroy = {
    }
}
