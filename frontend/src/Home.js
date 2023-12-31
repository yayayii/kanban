import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import {Link} from 'react-router-dom';

const Home = () => {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);

        fetch('/api', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            setTasks(data);
            setLoading(false);
        })
    }, []);

    const remove = async (task) => {
        await fetch('/api/' + (task.subtasks != null ? 'epictasks' : 'tasks') + '?id=' + task.id, {
            method: 'DELETE'
        }).then(() => {
            window.location.reload();
        });
    }

    const removeAll = async () => {
        await fetch(`/api`, {
            method: 'DELETE'
        }).then(() => {
            setTasks([]);
        });
    }

    const taskList = tasks.map(task => {
        return <tr key={task.id}>
            <td style={{overflow: "hidden", textOverflow: "ellipsis"}}>{task.name}</td>
            <td>{task.status}</td>
            <td>{task.endTime}</td>
            <td>
                <ButtonGroup>
                    <Button size="sm" color="primary" tag={Link} to={'/view/' + (task.subtasks != null ? 'epictasks' : 'tasks') + '/' + task.id}>View</Button>
                    <Button size="sm" color="warning" tag={Link} to={'/edit/' + (task.subtasks != null ? 'epictasks' : 'tasks') + '/' + task.id}>Edit</Button>
                    <Button size="sm" color="danger" onClick={() => remove(task)}>Delete</Button>
                </ButtonGroup>
            </td>
        </tr>
    });

    if (loading) {
        return <p>Loading...</p>;
    }

    return (
        <div>
            <Container fluid>
                <div style={{maxWidth: "75%", marginLeft: "auto", marginRight: "auto"}}>
                    <Table className="mt-4" style={{tableLayout: "fixed"}}>
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Status</th>
                            <th>End time</th>
                            <th width="10%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {taskList}
                        </tbody>
                    </Table>
                </div>
            </Container>
            <div align="center" style={{marginBottom: "10px"}}>
                <ButtonGroup>
                    <Button color="success" tag={Link} to="/create">Add task</Button>
                    <Button color="danger" onClick={() => removeAll()}>Clear</Button>
                </ButtonGroup>
            </div>
        </div>
    );
};

export default Home;