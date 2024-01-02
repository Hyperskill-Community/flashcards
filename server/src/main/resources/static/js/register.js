function register() {
  let object = {
    "email": document.getElementById("username").value,
    "password": document.getElementById("password").value,
  };

  let json = JSON.stringify(object);

  let xhr = new XMLHttpRequest();
  xhr.open("POST", '/api/register', false)
  xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
  xhr.send(json);

  if (xhr.status === 200) {
    const success = document.getElementById("success");
    success.hidden = false;
  } else {
    alert(`Error registering user! Status: ${xhr.status} response: ${xhr.responseText}`);
  }
}