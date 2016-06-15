(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('MissionDialogController', MissionDialogController);

    MissionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Mission', 'MissionStatus'];

    function MissionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Mission, MissionStatus) {
        var vm = this;

        vm.mission = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.missionstatuses = MissionStatus.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mission.id !== null) {
                Mission.update(vm.mission, onSaveSuccess, onSaveError);
            } else {
                Mission.save(vm.mission, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:missionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateStarted = false;
        vm.datePickerOpenStatus.dateCompleted = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
