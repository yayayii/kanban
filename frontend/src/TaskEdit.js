import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import {Link, useLocation, useNavigate, useParams} from 'react-router-dom';
import {useEffect, useState} from "react";
import Select from "react-select";

const TaskEdit = () => {
    const initialFormState = {
        name: '',
        description: '',
        status: ''
    };
    const location = useLocation();
    const [task, setTask] = useState(initialFormState);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    let isUpdated = false;
    const id = useParams();
    const statusOptions = [
        { value: "New", label: "New" },
        { value: "In progress", label: "In progress" },
        { value: "Done", label: "Done" }
    ];

    useEffect(() => {
        setLoading(true);

        fetch('/api/' + (location.pathname.includes('epictasks') ? 'epictasks' : location.pathname.includes('subtasks') ? 'subtasks' : 'tasks') + '?id=' + id.id, {
            method: 'GET',
            headers: {'Accept': 'application/json'},
        })
            .then(response => response.json())
            .then(data => {
                setTask(data);
                setLoading(false);
            });
    }, [id, setTask]);

    const handleChange = (event) => {
        const {name, value} = event.target;
        setTask(({...task, [name]: value}));
    };

    const handleChangeStatus = (event: string[]) => {
        setTask(({...task, ["status"]: event.value}));
    };

    const submit = async (event) => {
        event.preventDefault();

        await fetch('/api/' + (location.pathname.includes('epictasks') ? 'epictasks' : location.pathname.includes('subtasks') ? 'subtasks' : 'tasks') + '?id=' + id.id, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(task)
        })
        .then((response) => {
            if (response.status === 200) {
                isUpdated = true;
            } else {
                return response.json();
            }
        })
        .then((data) => {
            if (isUpdated) {
                setTask(initialFormState);
                navigate('/');
            } else {
                setError(data.message);
            }
        });
    };

    if (loading) {
        return <p>Loading...</p>;
    }

    return (
        <div>
            <Container style={{marginTop: "3%"}}>
                <Form onSubmit={submit}>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={task.name || ''} onChange={handleChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="description">Description</Label>
                        <Input type="textarea" name="description" id="description" value={task.description || ''}
                               onChange={handleChange}/>
                    </FormGroup>
                    {
                        !location.pathname.includes('epictasks') ?
                            <FormGroup>
                                <Label for="status">Status</Label>
                                <Select name="status" id="status" options={statusOptions} onChange={handleChangeStatus}
                                        defaultValue={statusOptions.filter(({value}) => value === task.status)}/>
                            </FormGroup>
                            : ''
                    }
                    { error ? <span style={{ color: 'red', fontSize: '12px'}}>{error}</span> : '' }
                    <FormGroup>
                        <div align="center" style={{marginBottom: "10px"}}>
                            <ButtonGroup>
                                <Button size="sm" color="primary" type="submit">Save</Button>
                                <Button size="sm" color="secondary" tag={Link} to={"/"}>Back</Button>
                            </ButtonGroup>
                        </div>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    );
};

export default TaskEdit;
