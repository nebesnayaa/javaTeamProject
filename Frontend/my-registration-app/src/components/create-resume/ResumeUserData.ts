export interface ResumeUserData {
  id: string;
  template: number,
  fullName: string;
  position: string;
  objective: string;
  education: string;
  workExperience: string;
  skillsAndAwards: string;
  languages: string;
  recommendations: string;
  hobbiesAndInterests: string;
  user: {
    age: number;
    gender: string;
    email: string;
    phone: string;
  }
}
  