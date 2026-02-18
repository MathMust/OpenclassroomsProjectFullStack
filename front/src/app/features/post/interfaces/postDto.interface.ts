import { CommentDto } from "./commentDto.interface";

export interface PostDto {
	id: number,
	date: Date,
	title: string,
	content: string,
	authorName: string,
	topicTitle: string,
	comments: CommentDto[]
}