import { TopicDto } from "../features/topic/interfaces/topicDto.interface";

export interface User {
	id: number,
	name: string,
	email: string,
	password: string,
	topics: TopicDto[]
}