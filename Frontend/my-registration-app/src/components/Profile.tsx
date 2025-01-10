import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Style/Profile.css";

const Profile: React.FC = () => {
  const [profileData, setProfileData] = useState({
    name: "",
    email: "",
    password: "",
  });

  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState(profileData);

  const [resumeData, setResumeData] = useState<null | { content: string }>({
    content: "",
  });
  const [resumeAvailable, setResumeAvailable] = useState(true);

  const [selectedTemplate, setSelectedTemplate] = useState<string>(""); // Состояние для выбранного шаблона
  const [templates] = useState([
    { id: "template1", name: "Template 1" },
    { id: "template2", name: "Template 2" },
    { id: "template3", name: "Template 3" },
  ]); // Пример массива шаблонов

  const navigate = useNavigate();

  useEffect(() => {
    // Fetch profile data
    axios
      .get("/api/profile", { withCredentials: true })
      .then((response) => {
        setProfileData(response.data);
        setFormData(response.data);
      })
      .catch((error) => console.error("Error fetching profile data:", error));
    axios
      .get("/api/resumes/user", { withCredentials: true })
      .then((response) => {
        setResumeData(response.data);
        setResumeAvailable(true);
      })
      .catch((error) => {
        console.error("No resume found:", error);
        setResumeAvailable(false);
      });
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSave = () => {
    axios
      .put("/api/profile", formData, { withCredentials: true })
      .then((response) => {
        setProfileData(response.data);
        setIsEditing(false);
      })
      .catch((error) => console.error("Error updating profile:", error));
  };

  const handleLogout = () => {
    axios
      .post("/api/logout", {}, { withCredentials: true })
      .then(() => {
        setProfileData({ name: "", email: "", password: "" });
        window.location.href = "/login";
      })
      .catch((error) => console.error("Error during logout:", error));
  };

  const handleCreateResume = () => {
    navigate("/resume", { state: { templateId: selectedTemplate } }); // Передаем выбранный шаблон
  };

  const handleTemplateChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedTemplate(e.target.value);
  };

  return (
    <div>
      <div className="profilePanel">
        <nav className="navbar-profile">
          <a className="username">{profileData.name || "Username"}</a>
          <button className="btn-logout" onClick={handleLogout}>
            Logout
          </button>
        </nav>
      </div>
      <div className="wrapper">
        <div className="profile-container">
          <div className="navbar-profile">
            <h2>My profile</h2>
            {!isEditing ? (
              <a className="icon-edit" onClick={() => setIsEditing(true)}>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  height="24px"
                  viewBox="0 -960 960 960"
                  width="24px"
                  fill="#D9D9D9"
                >
                  <path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z" />
                </svg>
              </a>
            ) : (
              <></>
            )}
          </div>
          {!isEditing ? (
            <div className="infoFields">
              <p>Name: {profileData.name}</p>
              <p>Email: {profileData.email}</p>
              <p>Password: {profileData.password}</p>
            </div>
          ) : (
            <div className="infoFields">
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                placeholder="Name"
              />
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                placeholder="Email"
              />
              <input
                type="text"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                placeholder="Password"
              />
              <div className="buttons-container">
                <button className="btn-save" onClick={handleSave}>
                  Save
                </button>
                <button
                  className="btn-cancel"
                  onClick={() => setIsEditing(false)}
                >
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
      <div className="resume-profile-container">
        <h3>My resume</h3>
        <label>Choose the template:</label>
        <select
          value={selectedTemplate}
          onChange={handleTemplateChange}
          className="template-select"
        >
          <option value="">Choose</option>
          {templates.map((template) => (
            <option key={template.id} value={template.id}>
              {template.name}
            </option>
          ))}
        </select>
        {resumeAvailable ? (
          <div>
            <p>{resumeData?.content}</p>
            <button>Edit resume</button>
          </div>
        ) : (
          <div className="create-resume">
            <p className="text">You haven't created any resume yet.</p>
            <button className="btn-add" onClick={handleCreateResume}>
              Create
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Profile;
