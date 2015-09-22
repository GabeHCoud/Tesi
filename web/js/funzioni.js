function isBlank(value) 
{
    if (value === null || value.length === 0)
          return true;
        for (var count = 0; count < value.length; count++) {
          if (value.charAt(count) !== " ") return false;
        }
        return true;                      
}

function IsNumeric(input)
{
    return (input - 0) == input && (''+input).replace(/^\s+|\s+$/g, "").length > 0;
}

function controlloReg() 
{
    var nome = document.fregister.nome.value;
    var cognome = document.fregister.cognome.value;
    var mail = document.fregister.mail.value;   
    var atpos = mail.indexOf("@");
    var dotpos = mail.lastIndexOf(".");
    //validazione email
    if (atpos<1 || dotpos<atpos+2 || dotpos+2>=mail.length) {
        alert("Inserire un indirizzo email valido!");
        return ;
    } else if(isBlank(nome)) {
        alert("Inserire il Nome!");
        return ;
    } else if(isBlank(cognome)) {
        alert("Inserire il Cognome!");
        return;
    } else{
    //se ha passato i controlli
    document.fregister.action = "UtenteNew.jsp";
    document.fregister.submit();

    }
 }
 
function controlloReg2() 
{
    var nome = document.fregister.nome.value;
    var cognome = document.fregister.cognome.value;
    var mail = document.fregister.mail.value;   
    var atpos = mail.indexOf("@");
    var dotpos = mail.lastIndexOf(".");
    //validazione email
    if (atpos<1 || dotpos<atpos+2 || dotpos+2>=mail.length) {
        alert("Inserire un indirizzo email valido!");
        return ;
    } else if(isBlank(nome)) {
        alert("Inserire il Nome!");
        return ;
    } else if(isBlank(cognome)) {
        alert("Inserire il Cognome!");
        return;
    } else{
    //se ha passato i controlli
    document.fregister.action = "Fattura.jsp";
    document.fregister.submit();

    }
}

function insert() {
    var mese = document.ins.mese.value; 
    var giorno=document.ins.giorno.value;

    if ((mese == 02) && (giorno > 29)) {
      alert("Giorno Errato.");
      return false;
    }   
    if ((mese == 04) && (giorno > 30)) {
      alert("Giorno Errato.");
      return false;
    }   
    if ((mese == 06) && (giorno > 30)) {
      alert("Giorno Errato.");
      return false;
    }   
    if ((mese == 09) && (giorno > 30)) {
      alert("Giorno Errato.");
      return false;
    }   
    if ((mese == 11) && (giorno > 30)) {
      alert("Giorno Errato.");
      return false;
    }   

    document.ins.action="let.jsp";
    document.ins.submit();

}

function validEmail(e) {
    var filter = /^\s*[\w\-\+_]+(\.[\w\-\+_]+)*\@[\w\-\+_]+\.[\w\-\+_]+(\.[\w\-\+_]+)*\s*$/;
    return String(e).search(filter) != -1;
}

function validateDouble(e)
{
    var filter = /\d+\.\d+/;
    return String(e).search(filter) != -1;
}

function validatePhone(e)
{
    var filter = /\d{3}\-\d{7}/;
    return String(e).search(filter) != -1;
}

function registerUserOnSubmit(form)    
{
    if(isBlank(form.nome.value))
    {        
        alert("Inserire un nome!");
        return false;
    }
    if(isBlank(form.cognome.value))
    {
        alert("Inserire un cognome!");
        return false;
    }
    if(isBlank(form.email.value))
    {
        alert("Inserire un'email!");
        return false;
    }    
    if(!validEmail(form.email.value))
    {
        alert("Indirizzo email non valido.");
        return false;
    }
    return true;
}

function deleteFatturaOnSubmit()
{
    var r = window.confirm("Attenzione! Si sta per eliminare una fattura, tutti i dati ad essa collegati andranno persi. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function editConsumoOnSubmit(form)
{
    if(!validateDouble(form.contributi.value))
    {
        alert("Inserire un numero nel formato *.* oppure *.0");
        return false;
    }
    if(!validateDouble(form.altri.value))
    {
        alert("Inserire un numero nel formato *.* oppure *.0");
        return false;
    }
    if(!validateDouble(form.abbonamenti.value))
    {
        alert("Inserire un numero nel formato *.* oppure *.0");
        return false;
    }
    if(!validateDouble(form.totale.value))
    {
        alert("Inserire un numero nel formato *.* oppure *.0");
        return false;
    }
    
    var r = window.confirm("Attenzione! I dati del consumo stanno per essere modificati. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function deleteUserOnSubmit()
{
    var r = window.confirm("Attenzione! Si sta per eliminare una utente, tutti i dati ad esso collegati andranno persi. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function editUserOnSubmit(form)    
{
    if(isBlank(form.nome.value))
    {        
        alert("Inserire un nome!");
        return false;
    }
    if(isBlank(form.cognome.value))
    {
        alert("Inserire un cognome!");
        return false;
    }
    if(isBlank(form.newemail.value))
    {
        alert("Inserire un'email!");
        return false;
    }    
    if(!validEmail(form.newemail.value))
    {
        alert("Indirizzo email non valido.");
        return false;
    }
    
    var r = window.confirm("Attenzione! L'utente sta per essere modificato. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function addTelefonoOnSubmit(form)
{
    if(!validatePhone(form.numero.value))
    {
        alert("Numero di telefono non valido. Inserire un numero nel formato 123-1234567");
        return false;
    }
    return true;
}

function deleteTelefonoOnSubmit()
{
    var r = window.confirm("Attenzione! Si sta per eliminare una numero di telefono, tutti i dati ad esso collegati andranno persi. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function editTelefonoOnSubmit(form)
{
    if(!validatePhone(form.newnumero.value))
    {
        alert("Numero di telefono non valido. Inserire un numero nel formato 123-1234567");
        return false;
    }
    var r = window.confirm("Attenzione! Il numero di telefono sta per essere modificato. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function deleteTContributoOnSubmit()
{
    var r = window.confirm("Attenzione! Si sta per dissociare il contributo dal numero di telefono. Confermi?");
    if(r === true) 
       return true;
    else return false;
}
function deleteTDispositivoOnSubmit()
{
    var r = window.confirm("Attenzione! Si sta per dissociare il dispositivo dal numero di telefono. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function addContributoOnSubmit(form)
{
    if(isBlank(form.nome.value))
    {
        alert("Inserire un nome!");
        return false;
    }   
    if(!validateDouble(form.costo.value))
    {
        alert("Costo non valido. Inserire un numero nel formato *.* o *.0");
        return false;
    }
    return true;
}

function deleteContributoOnSubmit()
{
    var r = window.confirm("Attenzione! Si sta per eliminare il contributo, tutti i dati ad esso collegati andranno persi. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function editContributoOnSubmit(form)
{
    if(isBlank(form.nome.value))
    {
        alert("Inserire un nome!");
        return false;
    }   
    if(!validateDouble(form.costo.value))
    {
        alert("Costo non valido. Inserire un numero nel formato *.* o *.0");
        return false;
    }
    var r = window.confirm("Attenzione! Il contributo sta per essere modificato. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function addDispositivoOnSubmit(form)
{
    if(isBlank(form.nome.value))
    {
        alert("Inserire un nome!");
        return false;
    }    
    if(!validateDouble(form.costo.value))
    {
        alert("Costo non valido. Inserire un numero nel formato *.* o *.0");
        return false;
    }
    return true;
}

function deleteDispositivoOnSubmit()
{
    var r = window.confirm("Attenzione! Si sta per eliminare il contributo, tutti i dati ad esso collegati andranno persi. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function editDispositivoOnSubmit(form)
{
    if(isBlank(form.nome.value))
    {
        alert("Inserire un nome!");
        return false;
    }   
    if(!validateDouble(form.costo.value))
    {
        alert("Costo non valido. Inserire un numero nel formato *.* o *.0");
        return false;
    }
    var r = window.confirm("Attenzione! Il dispositivo sta per essere modificato. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

function selectAll()
{
    var elements = document.getElementsByName("selectedUsers");
    for(i=0;i<elements.length;i++)
    {
        elements[i].checked = true;
    }    
}

function deselectAll()
{
    var elements = document.getElementsByName("selectedUsers");
    for(i=0;i<elements.length;i++)
    {
        elements[i].checked = false;
    }
}

function sendOnSubmit(form)
{      
    if(isBlank(form.sender.value))
    {
        alert("Inserire un'email!");
        return false;
    }    
    if(!validEmail(form.sender.value))
    {
        alert("Indirizzo email non valido.");
        return false;
    }
    if(isBlank(form.password.value))
    {
        alert("Inserire una password!");
        return false;
    }       
    
    var r = window.confirm("Attenzione! Si stanno per inviare le email. Confermi?");
    if(r === true) 
       return true;
    else return false;
}

