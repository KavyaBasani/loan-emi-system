const BASE_URL = "http://localhost:8081"; //  Spring Boot port

function createCustomer() {
    const name = document.getElementById("customerName").value;
    const email = document.getElementById("customerEmail").value;

    fetch(`${BASE_URL}/customers`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({name, email})
    })
    .then(res => res.json())
    .then(data => alert(`Customer created with ID: ${data.id}`))
    .catch(err => console.error(err));
}

function createLoan() {
    const loanType = document.getElementById("loanType").value;
    const principalAmount = parseFloat(document.getElementById("principalAmount").value);
    const tenureMonths = parseInt(document.getElementById("tenureMonths").value);
    const interestRate = parseFloat(document.getElementById("interestRate").value);
    const customerId = parseInt(document.getElementById("customerId").value);

    fetch(`${BASE_URL}/loans`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            loanType,
            principalAmount,
            tenureMonths,
            interestRate,
            customer: {id: customerId}
        })
    })
    .then(res => res.json())
    .then(data => alert(`Loan created with ID: ${data.id}`))
    .catch(err => console.error(err));
}

function getAllLoans() {
    fetch(`${BASE_URL}/loans`)
        .then(res => res.json())
        .then(loans => {
            const container = document.getElementById("loansContainer");
            container.innerHTML = ""; // clear previous content

            if (loans.length === 0) {
                container.innerHTML = "<p>No loans found.</p>";
                return;
            }

            loans.forEach(loan => {
                // create a card for each loan
                const div = document.createElement("div");
                div.className = "loan-card";
                div.innerHTML = `
                    <h3>Loan ID: ${loan.id} - ${loan.loanType}</h3>
                    <p><strong>Principal:</strong> ${loan.principalAmount}</p>
                    <p><strong>Tenure:</strong> ${loan.tenureMonths} months</p>
                    <p><strong>Interest Rate:</strong> ${loan.interestRate}%</p>
                    <p><strong>Customer ID:</strong> ${loan.customer.id}</p>
                    <button onclick="getEMIs(${loan.id})">Show EMIs</button>
                    <div id="emis-${loan.id}"></div>
                `;
                container.appendChild(div);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Failed to load loans. Check console for details.");
        });
}

function getEMIs(loanId) {
    fetch(`${BASE_URL}/loans/${loanId}/emis`)
        .then(res => res.json())
        .then(emis => {
            const container = document.getElementById(`emis-${loanId}`);
            container.innerHTML = ""; // clear previous EMIs

            if (emis.length === 0) {
                container.innerHTML = "<p>No EMIs found for this loan.</p>";
                return;
            }

            // Create "Pay Selected" button
            const paySelectedBtn = document.createElement("button");
            paySelectedBtn.textContent = "Pay Selected EMIs";
            paySelectedBtn.onclick = () => {
                const selectedIds = emis
                    .filter(emi => document.getElementById(`emi-checkbox-${emi.id}`).checked)
                    .map(emi => emi.id);
                
                if(selectedIds.length === 0) {
                    alert("Please select at least one EMI to pay.");
                    return;
                }

                fetch(`${BASE_URL}/emis/pay-multiple`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(selectedIds)
                })
                .then(res => res.json())
                .then(() => getEMIs(loanId))
                .catch(err => console.error(err));
            };
            container.appendChild(paySelectedBtn);

            // create table
            const table = document.createElement("table");
            const thead = document.createElement("thead");
            thead.innerHTML = `
                <tr>
                    <th>Select</th>
                    <th>ID</th>
                    <th>Amount</th>
                    <th>Due Date</th>
                    <th>Paid</th>
                    <th>Action</th>
                </tr>
            `;
            table.appendChild(thead);

            const tbody = document.createElement("tbody");

            emis.forEach(emi => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td><input type="checkbox" id="emi-checkbox-${emi.id}" ${emi.paid ? "disabled" : ""}></td>
                    <td>${emi.id}</td>
                    <td>${emi.amount.toFixed(2)}</td>
                    <td>${emi.dueDate}</td>
                    <td>${emi.paid ? "Yes" : "No"}</td>
                    <td>${emi.paid ? "" : `<button onclick="payEMI(${emi.id}, ${loanId})">Pay</button>`}</td>
                `;

                // color code paid/unpaid
                tr.style.backgroundColor = emi.paid ? "#d4edda" : "#f8d7da"; // green/red
                tbody.appendChild(tr);
            });

            table.appendChild(tbody);
            container.appendChild(table);
        })
        .catch(err => {
            console.error(err);
            alert("Failed to load EMIs. Check console for details.");
        });
}
function payEMI(emiId, loanId) {
    fetch(`${BASE_URL}/emis/${emiId}/pay`, {method: "PUT"})
        .then(res => res.json())
        .then(() => getEMIs(loanId))
        .catch(err => console.error(err));
}
function loadStats() {
    // Fetch total customers
    fetch(`${BASE_URL}/customers`)
        .then(res => res.json())
        .then(customers => {
            document.getElementById("totalCustomers").textContent = customers.length;
        })
        .catch(err => console.error(err));

    // Fetch total loans
    fetch(`${BASE_URL}/loans`)
        .then(res => res.json())
        .then(loans => {
            document.getElementById("totalLoans").textContent = loans.length;

            // Fetch all EMIs
            fetch(`${BASE_URL}/emis`)
                .then(res => res.json())
                .then(emis => {
                    document.getElementById("totalEMIs").textContent = emis.length;
                    const paid = emis.filter(e => e.paid).length;
                    document.getElementById("paidEMIs").textContent = paid;
                    document.getElementById("unpaidEMIs").textContent = emis.length - paid;
                })
                .catch(err => console.error(err));
        })
        .catch(err => console.error(err));
}
function loadEMIChart() {
    fetch(`${BASE_URL}/emis`)
        .then(res => res.json())
        .then(emis => {
            let paid = emis.filter(emi => emi.paid).length;
            let unpaid = emis.filter(emi => !emi.paid).length;

            const ctx = document.getElementById('emiChart').getContext('2d');

            // Destroy existing chart if already exists
            if (window.emiChartInstance) window.emiChartInstance.destroy();

            window.emiChartInstance = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: ['Paid', 'Unpaid'],
                    datasets: [{
                        label: 'EMI Status',
                        data: [paid, unpaid],
                        backgroundColor: ['#28a745', '#dc3545'], // green/red
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: { position: 'bottom' },
                        title: {
                            display: true,
                            text: 'Paid vs Unpaid EMIs'
                        }
                    }
                }
            });
        })
        .catch(err => console.error(err));
}