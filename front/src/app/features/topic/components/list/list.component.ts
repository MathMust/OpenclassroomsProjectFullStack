import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { TopicService } from '../../services/topic.service';
import { TopicsResponse } from '../../interfaces/topicsResponse.interface';
import { TopicDto } from '../../interfaces/topicDto.interface';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {

  public topics$: Observable<TopicsResponse> = this.topicService.getAll();

  constructor(private topicService: TopicService) { }

  toggleSubscription(topic: TopicDto) {
    if (topic.subscription) {
      // Appel API pour se désabonner
      this.topicService.unsubscribe(topic.id).subscribe({
        next: () => topic.subscription = false
      });
    } else {
      // Appel API pour s'abonner
      this.topicService.subscribe(topic.id).subscribe({
        next: () => topic.subscription = true
      });
    }
  }

}