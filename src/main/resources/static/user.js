async function getAuthUserJson() {
    let response = await fetch('http://localhost:8077/getAuthUser');
    if (response.ok) {
        return await response.json();
    } else {
        alert("Error HTTP: " + response.status);
    }
}

getAuthUserJson().then(user => {
    let nabBar = document.getElementById('navbar-content');
    let username = user['username'];
    let rolesString = '';
    for (let role of user['roles']) {
        rolesString += role.role + ' ';
    }

    nabBar.innerHTML = '<p class="navbar-text ms-0 my-0 px-0 text-light"> ' +
        username +  ' with roles: ' + rolesString + '</p>' +
        '<a class="nav-link me-0 text-light" href="/logout">logout</a>';


    const keys = Object.keys(user);
    let tableData = '<thead><tr>';

    for (let key of keys) {
        tableData += '<th><strong>' + key + '</strong></th>';
    }

    tableData += '</tr></thead>' + '<tbody>';
    tableData += '<tr id="user-' + user['id'] + '">';

    for (let key of keys) {
        tableData += '<td id="' + key + '-' + user['id'] + '" class="user-data">';
        if (key === 'roles') {
            let roles = user[key];

            for (let role of roles) {
                tableData += role['role'] + ' ';
            }

        } else {
            tableData += user[key];
        }
        tableData += '</td>'
    }

    tableData += '</tr>';

    tableData += '</tbody>';

    document.getElementById('user-info').innerHTML = tableData;
})

