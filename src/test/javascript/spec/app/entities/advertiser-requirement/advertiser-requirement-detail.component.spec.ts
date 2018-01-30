/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AdonlineTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AdvertiserRequirementDetailComponent } from '../../../../../../main/webapp/app/entities/advertiser-requirement/advertiser-requirement-detail.component';
import { AdvertiserRequirementService } from '../../../../../../main/webapp/app/entities/advertiser-requirement/advertiser-requirement.service';
import { AdvertiserRequirement } from '../../../../../../main/webapp/app/entities/advertiser-requirement/advertiser-requirement.model';

describe('Component Tests', () => {

    describe('AdvertiserRequirement Management Detail Component', () => {
        let comp: AdvertiserRequirementDetailComponent;
        let fixture: ComponentFixture<AdvertiserRequirementDetailComponent>;
        let service: AdvertiserRequirementService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AdonlineTestModule],
                declarations: [AdvertiserRequirementDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AdvertiserRequirementService,
                    JhiEventManager
                ]
            }).overrideTemplate(AdvertiserRequirementDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdvertiserRequirementDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdvertiserRequirementService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AdvertiserRequirement(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.advertiserRequirement).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
