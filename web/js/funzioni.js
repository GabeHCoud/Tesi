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

      