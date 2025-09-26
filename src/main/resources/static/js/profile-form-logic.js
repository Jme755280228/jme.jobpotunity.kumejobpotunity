// Function to get the current count of elements to determine the next index
function getNextIndex(containerId, className) {
    const container = document.getElementById(containerId);
    return container.querySelectorAll('.' + className).length;
}

// ----------------------
// 1. WORK EXPERIENCE LOGIC
// ----------------------
function addExperience() {
    const container = document.getElementById('experience-container');
    const index = getNextIndex('experience-container', 'experience-entry');

    // HTML Template for a new experience entry
    const newEntryHtml = `
        <div class="experience-entry" id="exp-${index}">
            <hr>
            <input type="hidden" name="jobExperiences[${index}].id" value="" />
            <div class="form-group">
                <label>Company</label>
                <input type="text" name="jobExperiences[${index}].companyName" class="form-control" required>
            </div>
            <div class="form-group">
                <label>Job Title</label>
                <input type="text" name="jobExperiences[${index}].jobTitle" class="form-control" required>
            </div>
            <div class="form-group">
                <label>Duration (e.g., 2020 - 2023)</label>
                <input type="text" name="jobExperiences[${index}].duration" class="form-control">
            </div>
            <div class="form-group">
                <label>Responsibilities/Achievements</label>
                <textarea name="jobExperiences[${index}].description" class="form-control" rows="3"></textarea>
            </div>
            <button type="button" onclick="removeExperience(this)">Remove</button>
        </div>
    `;
    
    container.insertAdjacentHTML('beforeend', newEntryHtml);
}

function removeExperience(button) {
    const entry = button.closest('.experience-entry');
    entry.remove();
    // After removal, re-index the remaining fields to maintain consistency (Advanced Step)
}

// ----------------------
// 2. EDUCATION LOGIC
// ----------------------
function addEducation() {
    const container = document.getElementById('education-container');
    const index = getNextIndex('education-container', 'education-entry');

    // HTML Template for a new education entry
    const newEntryHtml = `
        <div class="education-entry" id="edu-${index}">
            <hr>
            <input type="hidden" name="educationList[${index}].id" value="" />
            <div class="form-group">
                <label>Degree/Certification</label>
                <input type="text" name="educationList[${index}].degree" class="form-control" required>
            </div>
            <div class="form-group">
                <label>Institution Name</label>
                <input type="text" name="educationList[${index}].institution" class="form-control" required>
            </div>
            <div class="form-group">
                <label>Completion Year</label>
                <input type="number" name="educationList[${index}].completionYear" class="form-control" min="1900">
            </div>
            <button type="button" onclick="removeEducation(this)">Remove</button>
        </div>
    `;
    
    container.insertAdjacentHTML('beforeend', newEntryHtml);
}

function removeEducation(button) {
    const entry = button.closest('.education-entry');
    entry.remove();
    // After removal, re-index the remaining fields to maintain consistency (Advanced Step)
}
