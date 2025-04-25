import React, { useEffect, useState } from "react";
import { Typography, Card, Button, Spin, Input, message } from "antd";
import { Link } from "react-router-dom";

const { Title } = Typography;

const UserView = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [authorQuery, setAuthorQuery] = useState("");
    const [isSearching, setIsSearching] = useState(false);

    const fetchAllBooks = () => {
        setLoading(true);
        fetch('http://localhost:8080/api/v1/books')
            .then(res => res.json())
            .then(data => {
                setBooks(data);
                setLoading(false);
                setIsSearching(false);
            })
            .catch(err => {
                console.error("Ошибка при загрузке книг:", err);
                setLoading(false);
            });
    };

    const searchByAuthor = async () => {
        if (!authorQuery.trim()) {
            return void message.warning("Введите имя автора");
        }

        setLoading(true);
        setIsSearching(true);

        try {
            const res = await fetch(`http://localhost:8080/api/v1/books/by-author/${encodeURIComponent(authorQuery)}`);

            if (!res.ok) {
                if (res.status === 404) {
                    setBooks([]);
                    void message.info("Автор не найден");
                } else {
                    console.error(`Ошибка сервера: ${res.status}`);
                    void message.error("Ошибка на сервере");
                }
            } else {
                const data = await res.json();
                setBooks(data);

                if (data.length === 0) {
                    void message.info("У этого автора нет книг");
                }
            }
        } catch (err) {
            console.error("Ошибка при поиске:", err);
            void message.error("Произошла ошибка при поиске");
        } finally {
            setLoading(false);
        }
    };


    useEffect(() => {
        fetchAllBooks();
    }, []);

    if (loading) {
        return <div className="flex justify-center items-center h-[80vh]"><Spin size="large" /></div>;
    }

    return (
        <div className="p-6">
            <Title level={2}>Список книг</Title>

            <div className="mb-6 flex flex-col sm:flex-row gap-2 sm:items-center">
                <Input
                    placeholder="Введите имя автора"
                    value={authorQuery}
                    onChange={(e) => setAuthorQuery(e.target.value)}
                    className="sm:w-64"
                />
                <Button type="primary" onClick={searchByAuthor}>
                    Найти
                </Button>
                {isSearching && (
                    <Button onClick={fetchAllBooks}>
                        Сбросить поиск
                    </Button>
                )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {books.map(book => (
                    <Card
                        key={book.id}
                        title={book.title}
                        extra={
                            <Link to={`/user/book/${book.id}`}>
                                <Button type="primary">Подробнее</Button>
                            </Link>
                        }
                    >
                        <p><b>Авторы:</b> {book.authors?.map(author => author.name).join(', ')}</p>
                    </Card>
                ))}
            </div>

            {books.length === 0 && (
                <div className="text-center mt-10">
                    <Title level={4}>Книги не найдены.</Title>
                </div>
            )}
        </div>
    );
};

export default UserView;


