import React from "react";
import { Button, Card } from "antd";
import { useNavigate } from "react-router-dom";

const LoginSelector = () => {
    const navigate = useNavigate();

    return (
        <div className="flex justify-center items-center min-h-screen bg-gray-100 p-4">
            <div className="flex flex-col items-center gap-12 w-full max-w-3xl">

                {/* Блок выбора роли */}
                <Card
                    className="shadow-2xl rounded-2xl p-10 flex flex-col items-center"
                    style={{ width: 400, background: 'white' }}
                >
                    <h2 className="text-3xl font-semibold mb-8 text-center">Войти на сайт как:</h2>
                    <div className="flex flex-col gap-6 w-full">
                        <Button
                            type="primary"
                            size="large"
                            block
                            onClick={() => navigate("/author-login")}
                        >
                            Автор
                        </Button>
                        <Button
                            size="large"
                            block
                            onClick={() => navigate("/user")}
                        >
                            Пользователь
                        </Button>
                    </div>
                </Card>

                {/* Блок с описанием сайта */}
                <div className="max-w-xl text-center px-4">
                    <h2 className="text-3xl font-bold mb-4">Добро пожаловать на наш книжный портал!</h2>
                    <p className="text-lg text-gray-700 leading-relaxed mb-4">
                        Наша платформа — это уютное виртуальное пространство, где авторы обретают голос, а читатели — вдохновение.
                        Здесь вы сможете:
                    </p>
                    <ul className="list-disc list-inside text-gray-600 mb-4 space-y-2">
                        <li>Публиковать и редактировать свои произведения в несколько кликов;</li>
                        <li>Искать и сортировать книги по автору, жанру и популярности;</li>
                        <li>Оценивать и комментировать прочитанные главы;</li>
                        <li>Создавать персональные книжные полки и делиться рекомендациями.</li>
                    </ul>
                    <p className="italic text-gray-500">
                        Присоединяйтесь к нашему сообществу любителей чтения — напишите свою историю или найдите новую!
                    </p>
                </div>

            </div>
        </div>
    );
};

export default LoginSelector;



