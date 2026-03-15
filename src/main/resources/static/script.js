const API_USERS = "/api/users";
const API_CATEGORIES = "/api/categories";
const API_COURSES = "/api/courses";

document.getElementById("userForm").addEventListener("submit", createUser);
document.getElementById("categoryForm").addEventListener("submit", createCategory);
document.getElementById("courseForm").addEventListener("submit", createCourse);
document.getElementById("enrollForm").addEventListener("submit", enrollStudent);

async function createUser(e) {
    e.preventDefault();

    const payload = {
        fullName: document.getElementById("userFullName").value,
        email: document.getElementById("userEmail").value
    };

    const response = await fetch(API_USERS, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    const ok = await handleResponse(response, "Felhasználó sikeresen létrehozva.");
    if (ok) {
        document.getElementById("userForm").reset();
        await refreshAll();
    }
}

async function createCategory(e) {
    e.preventDefault();

    const payload = {
        name: document.getElementById("categoryName").value
    };

    const response = await fetch(API_CATEGORIES, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    const ok = await handleResponse(response, "Kategória sikeresen létrehozva.");
    if (ok) {
        document.getElementById("categoryForm").reset();
        await refreshAll();
    }
}

async function createCourse(e) {
    e.preventDefault();

    const instructorValue = document.getElementById("instructorId").value;

    const payload = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        durationInHours: parseInt(document.getElementById("durationInHours").value),
        instructorId: instructorValue ? parseInt(instructorValue) : null,
        categoryIds: getSelectedCategoryIds(),
        studentIds: getSelectedStudentIds()
    };

    const response = await fetch(API_COURSES, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    const ok = await handleResponse(response, "Kurzus sikeresen létrehozva.");
    if (ok) {
        document.getElementById("courseForm").reset();
        await refreshAll();
    }
}

async function enrollStudent(e) {
    e.preventDefault();

    const courseId = document.getElementById("enrollCourseId").value;
    const studentId = document.getElementById("enrollStudentId").value;

    if (!courseId || !studentId) {
        showMessage("Válassz kurzust és hallgatót is.", "error");
        return;
    }

    const response = await fetch(`${API_COURSES}/${courseId}/enroll/${studentId}`, {
        method: "POST"
    });

    const ok = await handleResponse(response, "Hallgató sikeresen feliratkoztatva.");
    if (ok) {
        document.getElementById("enrollForm").reset();
        await refreshAll();
    }
}

async function loadUsers() {
    const response = await fetch(API_USERS);
    const users = await response.json();

    renderUsers(users);
    populateUserSelects(users);
    document.getElementById("userCount").textContent = users.length;
}

async function loadCategories() {
    const response = await fetch(API_CATEGORIES);
    const categories = await response.json();

    renderCategories(categories);
    populateCategoryCheckboxes(categories);
    document.getElementById("categoryCount").textContent = categories.length;
}

async function loadCourses() {
    const response = await fetch(API_COURSES);
    const courses = await response.json();

    renderCourses(courses);
    populateCourseSelect(courses);
    document.getElementById("courseCount").textContent = courses.length;
}

function renderUsers(users) {
    const container = document.getElementById("userList");
    container.innerHTML = "";

    if (!users.length) {
        container.innerHTML = `<p class="empty-state">Még nincs felhasználó.</p>`;
        return;
    }

    users.forEach(user => {
        const card = document.createElement("div");
        card.className = "item-card";
        card.innerHTML = `
            <h3>${user.fullName}</h3>
            <p><strong>ID:</strong> ${user.id}</p>
            <p><strong>Email:</strong> ${user.email}</p>
        `;
        container.appendChild(card);
    });
}

function renderCategories(categories) {
    const container = document.getElementById("categoryList");
    container.innerHTML = "";

    if (!categories.length) {
        container.innerHTML = `<p class="empty-state">Még nincs kategória.</p>`;
        return;
    }

    categories.forEach(category => {
        const card = document.createElement("div");
        card.className = "item-card";
        card.innerHTML = `
            <h3>${category.name}</h3>
            <p><strong>ID:</strong> ${category.id}</p>
        `;
        container.appendChild(card);
    });
}

function renderCourses(courses) {
    const container = document.getElementById("courseList");
    container.innerHTML = "";

    if (!courses.length) {
        container.innerHTML = `<p class="empty-state">Még nincs kurzus.</p>`;
        return;
    }

    courses.forEach(course => {
        const categoryList = course.categories && course.categories.length
            ? course.categories.join(", ")
            : "Nincs megadva";

        const studentList = course.students && course.students.length
            ? course.students.join(", ")
            : "Nincs hallgató";

        const card = document.createElement("div");
        card.className = "course-card";
        card.innerHTML = `
            <h3>${course.title}</h3>
            <p><strong>ID:</strong> ${course.id}</p>
            <p><strong>Leírás:</strong> ${course.description || "-"}</p>
            <p><strong>Óraszám:</strong> ${course.durationInHours}</p>
            <p><strong>Oktató:</strong> ${course.instructorName} (${course.instructorEmail})</p>
            <p><strong>Kategóriák:</strong> ${categoryList}</p>
            <p><strong>Hallgatók:</strong> ${studentList}</p>
            <div class="course-actions">
                <button class="delete-btn" onclick="deleteCourse(${course.id})">Törlés</button>
            </div>
        `;
        container.appendChild(card);
    });
}

async function deleteCourse(id) {
    const response = await fetch(`${API_COURSES}/${id}`, {
        method: "DELETE"
    });

    const ok = await handleResponse(response, "Kurzus sikeresen törölve.");
    if (ok) {
        await refreshAll();
    }
}

function populateUserSelects(users) {
    const instructorSelect = document.getElementById("instructorId");
    const enrollStudentSelect = document.getElementById("enrollStudentId");
    const studentCheckboxContainer = document.getElementById("studentCheckboxes");

    if (!instructorSelect || !enrollStudentSelect || !studentCheckboxContainer) {
        console.error("Hiányzó frontend elem: instructorId, enrollStudentId vagy studentCheckboxes");
        return;
    }

    instructorSelect.innerHTML = `<option value="">Válassz oktatót</option>`;
    enrollStudentSelect.innerHTML = `<option value="">Válassz hallgatót</option>`;
    studentCheckboxContainer.innerHTML = "";

    if (!users.length) {
        studentCheckboxContainer.innerHTML = `<p class="empty-state">Nincs választható hallgató.</p>`;
        return;
    }

    users.forEach(user => {
        const instructorOption = document.createElement("option");
        instructorOption.value = user.id;
        instructorOption.textContent = `${user.fullName} (#${user.id})`;
        instructorSelect.appendChild(instructorOption);

        const enrollOption = document.createElement("option");
        enrollOption.value = user.id;
        enrollOption.textContent = `${user.fullName} (#${user.id})`;
        enrollStudentSelect.appendChild(enrollOption);

        const wrapper = document.createElement("label");
        wrapper.className = "checkbox-item";
        wrapper.innerHTML = `
            <input type="checkbox" value="${user.id}" class="student-checkbox">
            <span>${user.fullName} (#${user.id})</span>
        `;
        studentCheckboxContainer.appendChild(wrapper);
    });
}

function populateCategoryCheckboxes(categories) {
    const container = document.getElementById("categoryCheckboxes");
    container.innerHTML = "";

    if (!categories.length) {
        container.innerHTML = `<p class="empty-state">Nincs választható kategória.</p>`;
        return;
    }

    categories.forEach(category => {
        const wrapper = document.createElement("label");
        wrapper.className = "checkbox-item";
        wrapper.innerHTML = `
            <input type="checkbox" value="${category.id}" class="category-checkbox">
            <span>${category.name} (#${category.id})</span>
        `;
        container.appendChild(wrapper);
    });
}

function populateCourseSelect(courses) {
    const select = document.getElementById("enrollCourseId");
    select.innerHTML = `<option value="">Válassz kurzust</option>`;

    courses.forEach(course => {
        const option = document.createElement("option");
        option.value = course.id;
        option.textContent = `${course.title} (#${course.id})`;
        select.appendChild(option);
    });
}

function getSelectedCategoryIds() {
    return Array.from(document.querySelectorAll(".category-checkbox:checked"))
        .map(checkbox => parseInt(checkbox.value));
}

function getSelectedStudentIds() {
    return Array.from(document.querySelectorAll(".student-checkbox:checked"))
        .map(checkbox => parseInt(checkbox.value));
}

async function handleResponse(response, successMessage) {
    if (response.ok) {
        showMessage(successMessage, "success");
        return true;
    }

    let errorMessage = "Ismeretlen hiba történt.";

    try {
        const error = await response.json();
        if (error.error) {
            errorMessage = error.error;
        }
    } catch (e) {
        errorMessage = "Hiba történt a kérés feldolgozása során.";
    }

    showMessage(errorMessage, "error");
    return false;
}

function showMessage(message, type) {
    const box = document.getElementById("messageBox");
    box.textContent = message;
    box.className = `message ${type}`;

    setTimeout(() => {
        box.className = "message hidden";
        box.textContent = "";
    }, 4000);
}

async function refreshAll() {
    await loadUsers();
    await loadCategories();
    await loadCourses();
}

refreshAll();