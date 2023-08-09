document.addEventListener("DOMContentLoaded", function () {
    var unavailableDates = Array.from(document.getElementById("unavailableDatesList").options).map(function (option) {
        return option.value;
    });


    var startDate = null;
    var endDate = null;

    var calendar = flatpickr("#dateRangeCalendar", {
        inline: true,
        mode: "range",
        dateFormat: "Y-m-d",
        minDate: new Date(),
        disable: unavailableDates,
        clickOpens: true,
        onChange: function (selectedDates, dateStr, instance) {
            startDate = selectedDates[0];
            endDate = selectedDates[1];

            document.getElementById("startDateInput").value = startDate ? startDate.toISOString().split('T')[0] : "";
            document.getElementById("endDateInput").value = endDate ? endDate.toISOString().split('T')[0] : "";


        },

        locale: {
            weekdays: {
                shorthand: ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"],
                longhand: [
                    "Domingo",
                    "Lunes",
                    "Martes",
                    "Miércoles",
                    "Jueves",
                    "Viernes",
                    "Sábado"
                ]
            },
            months: {
                shorthand: [
                    "Ene",
                    "Feb",
                    "Mar",
                    "Abr",
                    "May",
                    "Jun",
                    "Jul",
                    "Ago",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dic"
                ],
                longhand: [
                    "Enero",
                    "Febrero",
                    "Marzo",
                    "Abril",
                    "Mayo",
                    "Junio",
                    "Julio",
                    "Agosto",
                    "Septiembre",
                    "Octubre",
                    "Noviembre",
                    "Diciembre"
                ]
            }
        }
    });


});