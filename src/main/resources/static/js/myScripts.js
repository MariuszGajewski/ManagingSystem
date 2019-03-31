var monthSelect = document.getElementById("months");
var yearsSelect = document.getElementById("years");
var daysSelect = document.getElementById("days");
var isDateGenerated =false;
var selectedDayValue

function confirmDel(value) {
    var deleteExam = window.confirm("Do you really want delete this user?");
    if(deleteExam == true) {
        value.submit();
    }
    else{
        location.href ='/userManager';
    }
}

function confirmGroupDelete(value) {
    var deleteExam = window.confirm("Do you really want delete this group?");
    if(deleteExam == true) {
        value.submit();
    }
    else{
        location.href ='/groupManager';
    }
}

function getYearsList(){
    for (var i = 0; i < 100; i++) {
        var option = document.createElement("option");
        option.value = 2018 -i;
        option.text = 2018 -i;
        yearsSelect.appendChild(option);
    }

}
function getMothsList(){
    var array = ["January", "February","March", "April", "May", "June", "July", "August", "September","October","November","December"];
    for (var i = 0; i < array.length; i++) {
        var option = document.createElement("option");
        option.value = i+1;
        option.text = array[i];
        monthSelect.appendChild(option);
    }
}
function getDaysList(month){
    var yearValue = yearsSelect.options[yearsSelect.selectedIndex].value;
    var days= new Date(yearValue,month,0).getDate();
    for( i = daysSelect.length - 1 ; i > 0 ; i--){
        daysSelect.remove(i);
    }
    for (var i = 1; i <= days; i++) {
        var option = document.createElement("option");
        option.value = i;
        option.text = i;
        daysSelect.appendChild(option);
    }
    if(isDateGenerated ==true){
        daysSelect.selectedIndex = selectedDayValue;
    }

}
function daySelect() {
    yearsSelect.addEventListener("change",createDate);
    monthSelect.addEventListener("change",createDate);
    createDate();
}
function createDate(){
    var yearValue = yearsSelect.options[yearsSelect.selectedIndex].value;
    var monthValue = monthSelect.options[monthSelect.selectedIndex].value;
    var dayValue = daysSelect.options[daysSelect.selectedIndex].value;
    var date = new Date(Date.UTC(yearValue,monthValue-1,dayValue));
    var dateInput = document.getElementById("date");
    var dateInputHidden = document.getElementById("hiddenDate");
    dateInput.value =date.toISOString().substr(0,10);
    dateInputHidden.value =date.toISOString().substr(0,10);
    isDateGenerated = true;
    selectedDayValue = dayValue;

}

function showHint(inputId) {
    var inputHint = document.getElementById(inputId.id+"Hint");
    inputHint.className= "showHint";

}
function hideHint(inputId) {
    var inputHint = document.getElementById(inputId.id+"Hint");
    inputHint.className= "hideHint";
}


function validateForm() {
    var error = document.getElementById("errors");
    var formInputs = document.getElementsByClassName("form-control");
    error.innerHTML ="";
    var input;
    var errors = false;
    for (var i = 0; i < formInputs.length -1 ; i++) {
        input = formInputs[i];
        console.log(input);
        if (!input.validity.valid) {
            error.innerHTML += input.validationMessage +" " +input.id +"</br>";
            errors = true;
        }
    }
    if(document.getElementById("password").value !== document.getElementById("rpassword").value){

        errors = true;
        error.innerHTML += "Given passwords are different <br/>";
    }
    if(document.getElementById("hiddenDate").value ===""){
        errors = true;
        error.innerHTML  += "Please select birth date <br/>";
    }
    if(errors === true){
        return false;
    }
}

function addGroup() {
    document.getElementById("addGroup").style.display= "block";
}