import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { AdvertiserRequirement } from './advertiser-requirement.model';
import { AdvertiserRequirementService } from './advertiser-requirement.service';

@Component({
    selector: 'jhi-advertiser-requirement-detail',
    templateUrl: './advertiser-requirement-detail.component.html'
})
export class AdvertiserRequirementDetailComponent implements OnInit, OnDestroy {

    advertiserRequirement: AdvertiserRequirement;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private advertiserRequirementService: AdvertiserRequirementService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAdvertiserRequirements();
    }

    load(id) {
        this.advertiserRequirementService.find(id).subscribe((advertiserRequirement) => {
            this.advertiserRequirement = advertiserRequirement;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAdvertiserRequirements() {
        this.eventSubscriber = this.eventManager.subscribe(
            'advertiserRequirementListModification',
            (response) => this.load(this.advertiserRequirement.id)
        );
    }
}
