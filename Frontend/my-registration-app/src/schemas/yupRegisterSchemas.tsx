import * as yup from "yup";

const MIN = 5;
const MAX = 10;
const PASS_VALUE = 8;
const yupRegisterSchemas = yup.object({
    name: yup.string()
    .required("Имя надо"),
    Login: yup.string()
    .min(MIN, `Логин меньше ${MIN} символов`)
    .max(MAX,`Логин больше ${MAX} символов`)
    .required("логин надо"),
    email: yup.string()
    .email("ну надо брат")
    .required("Заполни по братски"),
    password: yup.string()
    .min(MIN, `соберись надо всего ${MIN}`)
    .required("давай"),
    confirmPassword: yup.string()
    .required("ПУСТО")
    .oneOf([yup.ref("password")]),

});

export default yupRegisterSchemas;